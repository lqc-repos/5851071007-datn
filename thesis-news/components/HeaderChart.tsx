/* eslint-disable no-shadow */
import { useEffect, useState } from "react";
import moment from "moment";
import { rangeDateByDays } from "@/constant";
import DateRangeInput from "./DateRangeInput";

interface ChartHeadeProps {
  defaultDays?: number;
  className?: string;
  onChangeDateRange?: (_value: [string, string]) => void;
  onChangeView?: () => void;
}

const formatDate = "MM/DD/YYYY";
export const showDataLabels = (fromDate: string, toDate: string) => {
    // 'DD/MM
    const fomatLabels = moment(fromDate).year() === moment(toDate).year() ? 'DD/MM' : 'DD/MM/YYYY';
    return rangeDateByDays(fromDate, toDate, fomatLabels);
};

const HeaderChart = ({
  className = "",
  defaultDays = 0,
  onChangeDateRange = () => {},
  onChangeView = () => {},
}: ChartHeadeProps) => {
  const toDate = moment().format(formatDate);
  const [dateRange, setDateRange] = useState<[string, string]>(["", ""]);

  const handChangeDateRange = (rangeDate: [string, string]) => {
    setDateRange(rangeDate);
    onChangeDateRange(rangeDate);
  };

  useEffect(() => {
    const fromDate = moment(toDate)
      .add(-defaultDays, "days")
      .format(formatDate);
    setDateRange([fromDate, toDate]);
  }, []);

  return (
    <div className={`font-[500] text-[14px] ${className}`}>
      <div className="text-center w-full my-4">
        <DateRangeInput
          className="mt-2 block justify-center"
          values={[dateRange[0], dateRange[1]]}
          onChange={(_dateRange) => {
            const [toDate, fromDate] = _dateRange;
            handChangeDateRange([toDate, fromDate]);
          }}
          maxDate={new Date()}
        />
      </div>
    </div>
  );
};

export default HeaderChart;
