import { formatDate, formatDateHourMinute } from "@/lib/format";
import { Divider } from "@mui/joy";
import { Chip } from "@mui/material";
import Image from "next/image";
import Link from "next/link";
import BookmarkIcon from "@mui/icons-material/Bookmark";
import BookmarkBorderIcon from "@mui/icons-material/BookmarkBorder";
import { usePersonStore } from "@/story";

const CardIndex: React.FC<{ data: [] | never[]; isStyle?: boolean }> = ({
  data,
  isStyle = false,
}) => {
  const userData: any = usePersonStore((state: any) => state.user);
  return (
    <>
      <div className={`${isStyle ? "" : "pt-8"} block`}>
        <div>
          <div
            className={`max-h-[82vh] ${
              isStyle ? "scroll-visible !pl-0" : "scroll-visible"
            }`}
          >
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
                                      label={el.topics[0] || ""}
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
                                  {formatDateHourMinute(
                                    el.publicationDate * 1000
                                  )}
                                </div>
                                <div className="cursor-pointer">
                                  {userData &&
                                  userData?.member?.savedArticles.length > 0 &&
                                  userData?.member?.savedArticles.includes(
                                    el.id
                                  ) ? (
                                    <BookmarkIcon />
                                  ) : (
                                    <>
                                      {!userData ? "" : <BookmarkBorderIcon />}
                                    </>
                                  )}
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
                                                {el.title}
                                              </div>
                                              <div className="block">
                                                <p className="jy max-h-28 leading-5 visible gg ge gd overflow-hidden text-base tracking-normal">
                                                  {el.description}
                                                </p>
                                              </div>
                                            </Link>
                                          </div>
                                        </div>
                                        <div className="lg:ml-14 block ">
                                          <Link href={`/article/${el.id}`}>
                                            <Image
                                              alt="image"
                                              src={
                                                el.images &&
                                                el.images.length > 0
                                                  ? el?.images[0].url
                                                  : "/No_Image_Available.jpg"
                                              }
                                              width={112}
                                              height={112}
                                            />
                                          </Link>
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
      </div>
    </>
  );
};

export default CardIndex;
