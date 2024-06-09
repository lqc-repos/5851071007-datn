"use client";
import { Button } from "@mui/material";
import { useState } from "react";
import DatePicker from "react-datepicker";
import { useRouter } from "next/navigation";

const DateCustom: React.FC = () => {
  const router = useRouter();
  const [startDate, setStartDate] = useState();
  const [endDate, setEndDate] = useState();

  const handleFilter = async () => {
    if (!startDate || !endDate) return;
    let queryParams = new URL(window.location.href);
    let search_params = queryParams.searchParams;
    const startDateFormat = new Date(startDate).getTime() / 1000;
    const endDateFormat = new Date(endDate).getTime() / 1000;
    search_params.set("startDate", `${startDateFormat}`);
    search_params.set("endDate", `${endDateFormat}`);
    router.replace(queryParams.search);
    const resp = await fetch(`/api/filter/${queryParams.search}`)
      .then((res) => res.json())
      .catch((e) => console.log(e));
    console.log(resp);
  };
  return (
    <>
      <DatePicker
        selected={startDate}
        onChange={(date: any) => setStartDate(date)}
        selectsStart
        startDate={startDate}
        endDate={endDate}
        className="p-2 border-solid border-[1px] rounded"
      />
      <span className="mx-1 flex items-center">-</span>
      <DatePicker
        selected={endDate}
        onChange={(date: any) => setEndDate(date)}
        selectsEnd
        startDate={startDate}
        endDate={endDate}
        minDate={startDate}
        className="p-2 border-solid border-[1px] rounded"
      />
      <Button variant="outlined" className="ml-3" onClick={handleFilter}>
        Lọc
      </Button>
    </>
  );
};

export default DateCustom;
