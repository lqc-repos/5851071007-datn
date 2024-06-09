"use client";

import DOMPurify from "dompurify";

const ContentPost: React.FC<{ data: string }> = ({ data }) => {
  const rawHTML = `${data.split('\n').join('<p class="mb-3" />')}`;
  return (
    <div dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(rawHTML) }} />
  );
};

export default ContentPost;
