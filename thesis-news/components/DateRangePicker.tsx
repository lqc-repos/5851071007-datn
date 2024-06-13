import Calendar, { CalendarProps } from "react-calendar";
import KeyboardArrowLeftIcon from "@mui/icons-material/KeyboardArrowLeft";
import KeyboardArrowRightIcon from "@mui/icons-material/KeyboardArrowRight";
import KeyboardDoubleArrowLeftIcon from "@mui/icons-material/KeyboardDoubleArrowLeft";
import KeyboardDoubleArrowRightIcon from "@mui/icons-material/KeyboardDoubleArrowRight";
import moment from "moment";

import "react-calendar/dist/Calendar.css";
import React from "react";

interface DateRangePickerProps
  extends Omit<CalendarProps, "onChange" | "value"> {
  onChange?: (_value: [string, string]) => void;
  value?: [string, string];
}

const DateRangePicker = ({
  className,
  onChange = () => {},
  value,
  ...rest
}: DateRangePickerProps) => (
  <Calendar
    className={`circa-calendar-picker ${className}`}
    view="month"
    // locale="vi-VN"
    nextLabel={<KeyboardArrowRightIcon />}
    next2Label={<KeyboardDoubleArrowRightIcon />}
    prevLabel={<KeyboardArrowLeftIcon />}
    prev2Label={<KeyboardDoubleArrowLeftIcon />}
    selectRange
    onChange={(dateRange: Date[]) => {
      onChange([moment(dateRange[0]).format(), moment(dateRange[1]).format()]);
    }}
    value={
      value
        ? [
            value[0] ? new Date(value[0]) : null,
            value[0] ? new Date(value[1]) : null,
          ]
        : value
    }
    {...rest}
  />
);

export default DateRangePicker;
