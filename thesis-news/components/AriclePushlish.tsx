"use client";
import { useEffect, useState } from "react";
import CardCustom from "./Card";
import { usePersonStore } from "@/story";
import CardIndex from "./CardIndex";
import { Box, CircularProgress, Pagination } from "@mui/material";

const AriclePushlish: React.FC = () => {
  const userData: any = usePersonStore((state: any) => state.user);

  const [data, setData] = useState([]);
  const [page, setPage] = useState<number>(1);
  const [isLoading, setIsLoading] = useState(false);
  const [totalData, setTotalData] = useState(1);

  const getPostSave = async () => {
    setIsLoading(true);
    const dataCookie = JSON.parse(localStorage.getItem("user") as any);
    const resp = await fetch(`http://localhost:8080/user/get_articles`, {
      method: "POST",
      mode: "cors",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        memberId: dataCookie?.member?.id || "",
        type: "published",
        page: page,
        size: 10,
      }),
    })
      .then((res) => res.json())
      .catch((e) => console.log(e));

    setData(resp?.data?.articles || []);
    setTotalData(resp?.data?.totalPage || 1);

    setTimeout(() => {
      setIsLoading(false);
    }, 500);
  };
  useEffect(() => {
    getPostSave();
  }, [page]);

  const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
  };
  return (
    <>
      <div className="block">
        <div className="flex flex-col">
          <div className="mb-4">
            <h1 className="font-bold text-2xl">Bài viết đã tạo</h1>
          </div>
          <div>
            {isLoading ? (
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  position: "relative",
                  top: "50px",
                }}
              >
                <CircularProgress />
              </Box>
            ) : (
              <>
                <CardIndex data={data} />
                <div className="block justify-end">
                  <Pagination
                    count={totalData}
                    variant="outlined"
                    shape="rounded"
                    className="block justify-end"
                    color="primary"
                    page={page}
                    onChange={handleChange}
                  />
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default AriclePushlish;