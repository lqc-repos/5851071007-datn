package thesis.core.news.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import thesis.core.news.account.Account;
import thesis.core.news.member.Member;
import thesis.core.news.role.Role;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountResponse {
    private Account account;
    private Member member;
    private Role role;
}
