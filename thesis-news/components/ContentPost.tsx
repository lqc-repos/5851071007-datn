"use client";

import { getRandomInt } from "@/lib/format";
import DOMPurify from "dompurify";

const ContentPost: React.FC<{ data: string; images: [] }> = ({
  data,
  images,
}) => {
  const dataFormat = data.split("\n");
  let indexRandom: any;
  if (images && images?.length > 0) {
    indexRandom = images.map(() => getRandomInt(dataFormat.length));
  }

  if (indexRandom?.length > 0) {
    indexRandom?.map((el: any) =>
      dataFormat.splice(
        el,
        0,
        `<img alt="image" src="https://i1-vnexpress.vnecdn.net/2022/01/24/GS-Nong-Van-Hai-6357-1642958872.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=B2yTyXRzXLHiAsAZFCW03Q" />`
      )
    );
  }

  const rawHTML = `${dataFormat.join('<p class="mb-3" />')}`;
  return (
    <div dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(rawHTML) }} />
  );
};

export default ContentPost;
