'use client'
/* eslint-disable react/no-unescaped-entities */
import { formatDate, formatDateHourMinute } from "@/lib/format";
import { Divider } from "@mui/joy";
import { Chip } from "@mui/material";
import Link from "next/link";
import { useEffect, useState } from "react";

const HightRate: React.FC<{ isStyle?: string }> = ({ isStyle = false }) => {
  const [data, setData] = useState([]);
  const getPostRandom = async () => {
    const resp: any = await fetch(
      "http://localhost:8080/user/random_articles",
      {
        method: "POST",
        mode: "cors",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ size: 5 }),
      }
    )
      .then((data) => data.json())
      .catch((e) => console.log(e));
    if (resp?.statusCode === 200) {
      setData(resp.data.articles || []);
    }
  };
  useEffect(() => {
    getPostRandom();
  }, []);

  return (
    <div className="flex-auto block">
      <div className="mt-10 block">
        <div className="mb-5 block">Bài viết liên quan</div>
        {data.length > 0 &&
          data.map((el: any) => (
            <article key={el.id || Math.floor(Math.random() * 1000)}>
              <div className="box-content mr-auto ml-auto block">
                <div className="justify-center flex ">
                  <div
                    className={`lg:max-w-[680px] mx-6 min-w-0 w-full ${
                      isStyle ? "ml-0" : ""
                    }`}
                  >
                    <div className="w-full h-full">
                      <div className="block">
                        <div className="flex items-center">
                          <div>
                            <Link href={`/article/${el.id}`}>
                              <div>
                                <Chip
                                  label={`${el.topics[0]}`}
                                  className="bg-[#f17b7b] text-white"
                                />
                              </div>
                            </Link>
                          </div>
                          <div className="w-full ml-2 flex flex-nowrap">
                            <div className="flex text-sm">
                              {formatDate(el.publicationDate * 1000)}
                            </div>
                            <div className="flex-auto block text-sm ml-1">
                              {formatDateHourMinute(el.publicationDate * 1000)}
                            </div>
                          </div>
                        </div>
                        <div className="mt-3 block">
                          <div className="block">
                            <div className="relative block">
                              <div className="block">
                                <div>
                                  <div className="flex">
                                    <div
                                      className="flex-auto block w-9/12"
                                      style={{ wordBreak: "break-word" }}
                                    >
                                      <div className="block">
                                        <Link href={`/article/${el.id}`}>
                                          <div className="lg:mb-2 block cursor-pointer font-bold tracking-tight text-base lg:max-h-[72px] leading-6 overflow-hidden gd ge gg">
                                            {el.description}
                                          </div>
                                        </Link>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <Divider component="li" className="my-3" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </article>
          ))}
      </div>
    </div>
  );
};

export default HightRate;
