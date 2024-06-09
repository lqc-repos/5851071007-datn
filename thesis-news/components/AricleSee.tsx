"use client";
import { useEffect, useState } from "react";
import { usePersonStore } from "@/story";
import { Box, CircularProgress, Pagination } from "@mui/material";
import CardIndex from "./CardIndex";

const ArticleSee: React.FC = () => {
  const userData: any = usePersonStore((state: any) => state.user);
  const [page, setPage] = useState<number>();
  const [data, setData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const getPostSee = async () => {
    setIsLoading(true);
    const resp = await fetch(`http://localhost:8080/user/get_articles`, {
      method: "POST",
      mode: "cors",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        memberId: userData.member.id,
        type: "viewed",
        page: page,
        size: 10,
      }),
    })
      .then((res) => res.json())
      .catch((e) => console.log(e));

    setData(resp || []);
    setTimeout(() => {
      setIsLoading(false);
    }, 500);
  };
  useEffect(() => {
    getPostSee();
  }, [page]);

  const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
  };

  return (
    <>
      <div className="block">
        <div className="flex flex-col">
          <div className="mb-4">
            <h1 className="font-bold text-2xl">Bài viết đã xem</h1>
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
                    count={10}
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

export default ArticleSee;
