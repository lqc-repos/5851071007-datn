"use client";
import ContentPost from "@/components/ContentPost";
import { formatDate, formatDateHourMinute } from "@/lib/format";
import { Divider } from "@mui/joy";
import { Chip } from "@mui/material";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";


const DetailArticle: React.FC = () => {
  const router = useParams();
  const [data, setData] = useState<any>();
  const getDataArticle = async () => {
    const resp = await fetch(`/api/article/${router?.id || ""}`)
      .then((res) => res.json())
      .catch((e) => console.log(e));

    setData(resp);
  };
  useEffect(() => {
    getDataArticle();
  }, []);

  if (!data) return <div></div>
  return (
    <>
      <div className="block mt-5">
        <div className="flex flex-col">
          <div className="block">
            <p className="font-bold text-4xl">{data?.title || ""}</p>
          </div>
          <Divider component="li" className="my-3" />
          <div className="block mb-10">
            <div className="flex items-center">
              <div>
                <Chip
                  label={data.topics[0] || ""}
                  className="bg-[#f17b7b] text-white"
                />
              </div>
              <div>
                <div className="ml-2 flex flex-nowrap">
                  <div className="flex-auto block text-sm ml-1">
                    {formatDateHourMinute(data.publicationDate * 1000)}
                  </div>
                  <div className="flex text-sm">
                    {formatDate(data.publicationDate * 1000)}
                  </div>
                </div>
              </div>
              <div className="ml-4">
                <span className="mr-2">Tác giả:</span>
                <span className="text-[#f17b7b]">{data.authors[0]}</span>
              </div>
            </div>
          </div>
          <ContentPost data={data.content} />
        </div>
      </div>
    </>
  );
};

export default DetailArticle;
