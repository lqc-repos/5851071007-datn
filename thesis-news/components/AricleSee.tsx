"use client";
import { useEffect, useState } from "react";
import CardCustom from "./Card";

const ArticleSee: React.FC = () => {
  // api đã xem
  // const [data, setData] = useState([]);
  // const [isLoading, setIsLoading] = useState(false);
  // const getPostSee = async () => {
  //   setIsLoading(true);
  //   const resp = await fetch(`/api/topic/?topics=${topic}&page=${page}`)
  //     .then((res) => res.json())
  //     .catch((e) => console.log(e));

  //   setData(resp || []);
  //   setTimeout(() => {
  //     setIsLoading(false);
  //   }, 500);
  // };
  // useEffect(() => {
  //   getPostSee();
  // }, []);
  return (
    <>
      <div className="block">
        <div className="flex flex-col">
          <div className="mb-4">
            <h1 className="font-bold text-2xl">Bài viết đã xem</h1>
          </div>
          <div>
            <CardCustom topic="Thời sự" />
          </div>
        </div>
      </div>
    </>
  );
};

export default ArticleSee;
