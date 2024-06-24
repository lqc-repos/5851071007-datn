"use client";
import { useEffect, useState } from "react";
import CardCustom from "./Card";
import { usePersonStore } from "@/story";
import CardIndex from "./CardIndex";
import { Box, CircularProgress, Pagination } from "@mui/material";
import LabelChart from "./LabelChart";

const PersonalReport: React.FC = () => {
  const [totalLabel, setTotalLabel] = useState(undefined);


  return (
    <>
      <div className="block">
        <div className="flex flex-col">
          <div className="mb-4">
            <h1 className="font-bold text-2xl">Thống kê tìm kiếm</h1>
          </div>
          <div>
            {<>
              <LabelChart
                className="min-w-[500px]"
                title="Thống kê tìm kiếm"
                setTotalLabel={(e: any) => setTotalLabel(e)}
              />
            </>}
          </div>
        </div>
      </div>
    </>
  );
};

export default PersonalReport;