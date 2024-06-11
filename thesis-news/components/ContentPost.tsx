"use client";

import DOMPurify from "dompurify";

const ContentPost: React.FC<{ data: string; images: any }> = ({
  data,
  images,
}) => {
  const dataFormat = data.split("\n");

  // Chỉ số cho hình ảnh
  let imageIndex = 0;

  // Chèn hình ảnh và mô tả vào vị trí sao cho mỗi hình cách nhau ít nhất 1 content
  if (images) {
    for (
      let i = 1;
      i < dataFormat.length && imageIndex < images?.length;
      i += 2
    ) {
      const imageElement = `
        <div style="text-align: center;">
          <img alt=""image"" src=${images[imageIndex]?.url || ""} />
          <p style="margin-bottom: 5px;"><em>${
            images[imageIndex]?.description || ""
          }</em></p>
        </div>`;
      dataFormat.splice(i, 0, imageElement);
      imageIndex++;
    }
  }

  const rawHTML = `${dataFormat.join('<p class="mb-3" />')}`;
  return (
    <div dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(rawHTML) }} />
  );
};

export default ContentPost;
