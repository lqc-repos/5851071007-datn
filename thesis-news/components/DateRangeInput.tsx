import React, { useRef, useState } from "react";
import { CalendarProps } from "react-calendar";
import DateRangePickerInput from './DateRangePickerInput';
import ClearIcon from "@mui/icons-material/Clear";
import {
  ClickAwayListener,
  IconButton,
  InputAdornment,
  OutlinedInput,
} from "@mui/material";
import moment from "moment";
import { CalendarIcon } from "@/public/admin";

interface DateRangePickerInputProps
  extends Omit<CalendarProps, "onChange" | "value"> {
  className?: string;
  classNameWrap?: string;
  classNameFirstDay?: string;
  classNameLastDay?: string;
  values: [string, string];
  placeholderFrom?: string;
  placeholderTo?: string;
  onChange?: (_value: [string, string]) => void;
}

const DateRangeInput = ({
  className = "",
  classNameWrap = "",
  classNameFirstDay = "",
  classNameLastDay = "",
  placeholderFrom = "Từ Ngày",
  placeholderTo = "Đến Ngày",
  values,
  onChange = () => {},
  ...rest
}: DateRangePickerInputProps) => {
  const [open, setOpen] = useState(false);
  const boxRef = useRef(null);

  const handleClick = () => {
    setOpen(true);
  };

  return (
    <ClickAwayListener
      onClickAway={() => {
        if (open) setOpen(false);
      }}
    >
      <div className={`nguyen relative flex ${className}`} ref={boxRef}>
        <div className={`flex gap-3 ${classNameWrap}`}>
          <OutlinedInput
            className={`w-[166px] ${classNameFirstDay}`}
            readOnly
            size="small"
            sx={{
              cursor: "pointer",
              backgroundColor: "#FFF",
              "& input": {
                cursor: "pointer",
                fontSize: "14px"
              },
            }}
            placeholder={placeholderFrom}
            startAdornment={
              <InputAdornment position="start" className="text-primary">
                <CalendarIcon fill="#E88438" width={18} height={18} />
              </InputAdornment>
            }
            endAdornment={
              values[0] && (
                <IconButton
                  onClick={(e) => {
                    e.stopPropagation();
                    onChange(["", ""]);
                  }}
                  sx={{ p: "2px", width: "18px", height: "18px" }}
                >
                  <ClearIcon fontSize="small" />
                </IconButton>
              )
            }
            onClick={handleClick}
            value={values[0] ? moment(values[0]).format("DD/MM/YYYY") : ""}
          />

          <OutlinedInput
            className={`w-[166px] cursor-pointer ${classNameLastDay}`}
            readOnly
            size="small"
            sx={{
              cursor: "pointer",
              backgroundColor: "#FFF",
              "& input": {
                cursor: "pointer",
                fontSize: "14px"
              },
            }}
            startAdornment={
              <InputAdornment position="start" className="text-primary">
                <CalendarIcon fill="#E88438" width={18} height={18} />
              </InputAdornment>
            }
            endAdornment={
              values[0] && (
                <IconButton
                  onClick={(e) => {
                    e.stopPropagation();
                    onChange(["", ""]);
                  }}
                  sx={{ p: "2px", width: "18px", height: "18px" }}
                >
                  <ClearIcon fontSize="small" />
                </IconButton>
              )
            }
            placeholder={placeholderTo}
            onClick={handleClick}
            value={values[1] ? moment(values[1]).format("DD/MM/YYYY") : ""}
          />
        </div>

        <DateRangePickerInput
          anchorEl={boxRef.current as unknown as Element}
          value={values}
          onChange={(value) => {
            onChange(value);
            setOpen(false);
          }}
          open={open}
          className={className}
          {...rest}
        />
      </div>
    </ClickAwayListener>
  );
};

export default DateRangeInput;
