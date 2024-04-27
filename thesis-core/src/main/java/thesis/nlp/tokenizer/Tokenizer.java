package thesis.nlp.tokenizer;

import thesis.common.constant.TokenizerConst;
import thesis.common.utils.RegexUtils;
import thesis.common.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    /**
     * @param s
     * @return List of tokens from s
     * @throws IOException
     */
    public static List<String> tokenize(String s) throws IOException {
        if (s == null || s.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String[] tempTokens = s.trim().split("\\s+");
        if (tempTokens.length == 0) {
            return new ArrayList<String>();
        }

        List<String> tokens = new ArrayList<String>();

        for (String token : tempTokens) {
            if (token.length() == 1 || !StringUtils.hasPunctuation(token)) {
                tokens.add(token);
                continue;
            }

            if (token.endsWith(",")) {
                tokens.addAll(tokenize(token.substring(0, token.length() - 1)));
                tokens.add(",");
                continue;
            }

            if (TokenizerConst.VN_ABBREVIATION.contains(token)) {
                tokens.add(token);
                continue;
            }


            if (token.endsWith(".") && Character.isAlphabetic(token.charAt(token.length() - 2))) {
                if ((token.length() == 2 && Character.isUpperCase(token.charAt(token.length() - 2))) || (Pattern.compile(RegexUtils.SHORT_NAME).matcher(token).find())) {
                    tokens.add(token);
                    continue;
                }
                tokens.addAll(tokenize(token.substring(0, token.length() - 1)));
                tokens.add(".");
                continue;
            }

            if (TokenizerConst.VN_EXCEPTION.contains(token)) {
                tokens.add(token);
                continue;
            }

            boolean tokenContainsAbb = false;
            for (String e : TokenizerConst.VN_ABBREVIATION) {
                int i = token.indexOf(e);
                if (i < 0)
                    continue;

                tokenContainsAbb = true;
                tokens = recursive(tokens, token, i, i + e.length());
                break;
            }
            if (tokenContainsAbb)
                continue;

            boolean tokenContainsExp = false;
            for (String e : TokenizerConst.VN_EXCEPTION) {
                int i = token.indexOf(e);
                if (i < 0)
                    continue;

                tokenContainsExp = true;
                tokens = recursive(tokens, token, i, i + e.length());
                break;
            }
            if (tokenContainsExp)
                continue;

            List<String> regexes = RegexUtils.getRegexList();

            boolean matching = false;
            for (String regex : regexes) {
                if (token.matches(regex)) {
                    tokens.add(token);
                    matching = true;
                    break;
                }
            }
            if (matching) {
                continue;
            }

            for (int i = 0; i < regexes.size(); i++) {
                Pattern pattern = Pattern.compile(regexes.get(i));
                Matcher matcher = pattern.matcher(token);

                if (matcher.find()) {
                    if (i == RegexUtils.getRegexIndex("url")) {
                        String[] elements = token.split(Pattern.quote("."));
                        boolean hasURL = true;
                        for (String ele : elements) {
                            if (ele.length() == 1 && Character.isUpperCase(ele.charAt(0))) {
                                hasURL = false;
                                break;
                            }
                            for (int j = 0; j < ele.length(); j++) {
                                if (ele.charAt(j) >= 128) {
                                    hasURL = false;
                                    break;
                                }
                            }
                        }
                        if (hasURL) {
                            tokens = recursive(tokens, token, matcher.start(), matcher.end());
                        } else {
                            continue;
                        }
                    } else if (i == RegexUtils.getRegexIndex("month")) {
                        int start = matcher.start();

                        boolean hasLetter = false;

                        for (int j = 0; j < start; j++) {
                            if (Character.isLetter(token.charAt(j))) {
                                tokens = recursive(tokens, token, matcher.start(), matcher.end());
                                hasLetter = true;
                                break;
                            }
                        }

                        if (!hasLetter) {
                            tokens.add(token);
                        }
                    } else {
                        tokens = recursive(tokens, token, matcher.start(), matcher.end());
                    }

                    matching = true;
                    break;
                }
            }

            if (matching)
                continue;
            else
                tokens.add(token);
        }

        return tokens;
    }

    private static List<String> recursive(List<String> tokens, String token, int beginMatch, int endMatch)
            throws IOException {
        if (beginMatch > 0)
            tokens.addAll(tokenize(token.substring(0, beginMatch)));
        tokens.addAll(tokenize(token.substring(beginMatch, endMatch)));

        if (endMatch < token.length())
            tokens.addAll(tokenize(token.substring(endMatch)));

        return tokens;
    }

    public static List<String> joinSentences(List<String> tokens) {
        List<String> sentences = new ArrayList<>();

        List<String> sentence = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            String nextToken = null;
            if (i != tokens.size() - 1) {
                nextToken = tokens.get(i + 1);
            }
            String beforeToken = null;
            if (i > 0) {
                beforeToken = tokens.get(i - 1);
            }

            sentence.add(token);

            if (i == tokens.size() - 1) {
                sentences.add(joinSentence(sentence));
                return sentences;
            }

            if (i < tokens.size() - 2 && token.equals(TokenizerConst.COLON)) {
                if (Character.isDigit(nextToken.charAt(0)) && tokens.get(i + 2).equals(TokenizerConst.STOP)
                        || tokens.get(i + 2).equals(TokenizerConst.COMMA)) {
                    sentences.add(joinSentence(sentence));
                    sentence.clear();
                    continue;
                }
            }

            if (token.matches(RegexUtils.EOS_PUNCTUATION)) {

                // Added by Dat Quoc Nguyen
                if (nextToken.equals("\"") || nextToken.equals("''")) {
                    int count = 0;
                    for (String senToken : sentence) {
                        if (senToken.equals("\"") || senToken.equals("''"))
                            count += 1;
                    }
                    if (count % 2 == 1)
                        continue;
                }

                // If the current sentence is in the quote or in the brace
                if (StringUtils.isBrace(nextToken) || nextToken.isEmpty() || Character.isLowerCase(nextToken.charAt(0))
                        || nextToken.equals(TokenizerConst.COMMA) || Character.isDigit(nextToken.charAt(0))) {
                    continue;
                }

                // Sentence starts with its order number
                if (sentence.size() == 2 && token.equals(TokenizerConst.STOP)) {
                    if (Character.isDigit(beforeToken.charAt(0))) {
                        continue;
                    }
                    if (Character.isLowerCase(beforeToken.charAt(0))) {
                        continue;
                    }
                    if (Character.isUpperCase(beforeToken.charAt(0))) {
                        if (beforeToken.length() == 1) {
                            continue;
                        }
                    }
                }

                sentences.add(joinSentence(sentence));
                sentence.clear();
            }
        }

        return sentences;
    }

    public static String joinSentence(List<String> tokens) {
        StringBuffer sent = new StringBuffer();
        int length = tokens.size();
        String token;
        for (int i = 0; i < length; i++) {
            token = tokens.get(i);
            if (token.isEmpty() || token == null || token.equals(TokenizerConst.SPACE)) {
                continue;
            }
            sent.append(token);
            if (i < length - 1)
                sent.append(TokenizerConst.SPACE);
        }
        return sent.toString().trim();
    }
}
