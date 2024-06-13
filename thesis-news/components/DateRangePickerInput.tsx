import { CalendarProps } from "react-calendar";
import { Popper } from "@mui/material";
import React from "react";
import DateRangePicker from "./DateRangePicker";

interface DateRangePickerInputProps
  extends Omit<CalendarProps, "onChange" | "value"> {
  className?: string;
  onChange?: (_value: [string, string]) => void;
  value?: [string, string];
  anchorEl?: Element | null;
  open: boolean;
}

const DateRangePickerInput = ({
  className = "",
  onChange = () => {},
  anchorEl,
  open = false,
  value = ["", ""],
  ...rest
}: DateRangePickerInputProps) => (
  <Popper
    className={`z-50 !mt-2 overflow-hidden rounded-xl bg-white shadow-drop-sm ${className}`}
    open={open}
    anchorEl={anchorEl}
  >
    <DateRangePicker
      value={value}
      onChange={(nextValue) => {
        onChange(nextValue);
      }}
      {...rest}
    />
  </Popper>
);

export default DateRangePickerInput;
