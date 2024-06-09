"use client";
import { Box, CircularProgress, Pagination } from "@mui/material";
import React, { useEffect, useState } from "react";
import CardIndex from "./CardIndex";
import { useRouter } from "next/navigation";

const CardCustom: React.FC<{ topic: string }> = ({ topic }) => {
  const router = useRouter();
  const [data, setData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [page, setPage] = React.useState(1);
  const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
    let queryParams = new URL(window.location.href);
    let search_params = queryParams.searchParams;
    search_params.set("page", `${value}`);
    router.replace(queryParams.search);
    setPage(value);
  };

  const getData = async (page?: number) => {
    setIsLoading(true);
    const resp = await fetch(`/api/topic/?topics=${topic}&page=${page}`)
      .then((res) => res.json())
      .catch((e) => console.log(e));

    setData(resp || []);
    setTimeout(() => {
      setIsLoading(false);
    }, 500)
  };
  useEffect(() => {
    getData(page);
  }, [topic, page]);

  if (isLoading)
    return (
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
    );
  return (
    <>
      <CardIndex data={data} />
      {data.length > 0 && (
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
      )}
    </>
  );
};

export default CardCustom;
