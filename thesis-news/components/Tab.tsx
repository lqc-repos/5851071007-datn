"use client";
import { Box, Pagination, Tab, Tabs } from "@mui/material";
import React from "react";
import CardCustom from "./Card";
import Search from "./Search";
import { useRouter } from "next/navigation";
import { capitalizeFirstLetter } from "@/lib/format";

interface TabCustomProps {
  w?: string;
}

const defaultProps: TabCustomProps = {
  w: "100%",
};
const TabCustom: React.FC<TabCustomProps> = ({ w }) => {
  const router = useRouter();
  const [value, setValue] = React.useState(1);

  const handleChange = (
    event: React.SyntheticEvent | any,
    newValue: number
  ) => {
    if (event.target) {
      const text = capitalizeFirstLetter(event?.target?.innerText);
      router.push(`?topics=${text}`);
    }
    setValue(newValue);
  };
  return (
    <Box sx={{ width: w, typography: "body1" }}>
      <Tabs
        value={value}
        onChange={handleChange}
        variant="scrollable"
        scrollButtons
        allowScrollButtonsMobile
        aria-label="scrollable auto tabs example"
      >
        <Tab label="Tìm kiếm" />
        <Tab label="Thời sự" />
        <Tab label="Góc nhìn" />
        <Tab label="Thế giới" />
        <Tab label="Kinh doanh" />
        <Tab label="Bất động sản" />
        <Tab label="Khoa học" />
        <Tab label="Giải trí" />
        <Tab label="Thể thao" />
        <Tab label="Pháp luật" />
        <Tab label="Giáo dục" />
        <Tab label="Sức khỏe" />
        <Tab label="Đời sống" />
        <Tab label="Du lịch" />
        <Tab label="Số hóa" />
        <Tab label="Xe" />
        <Tab label="Ý kiến" />
        <Tab label="Tâm sự" />
      </Tabs>
      <div className="px-10">
        {value === 0 && <Search />}
        {value === 1 && <CardCustom topic="Thời sự" />}
        {value === 2 && <CardCustom topic="Góc nhìn" />}
        {value === 3 && <CardCustom topic="Thế giới" />}
        {value === 4 && <CardCustom topic="Kinh doanh" />}
        {value === 5 && <CardCustom topic="Bất động sản" />}
        {value === 6 && <CardCustom topic="Khoa học" />}
        {value === 7 && <CardCustom topic="Giải trí" />}
        {value === 8 && <CardCustom topic="Thể thao" />}
        {value === 9 && <CardCustom topic="Pháp luật" />}
        {value === 10 && <CardCustom topic="Giáo dục" />}
        {value === 11 && <CardCustom topic="Sức khỏe" />}
        {value === 12 && <CardCustom topic="Đời sống" />}
        {value === 13 && <CardCustom topic="Du lịch" />}
        {value === 14 && <CardCustom topic="Số hóa" />}
        {value === 15 && <CardCustom topic="Xe" />}
        {value === 16 && <CardCustom topic="Ý kiến" />}
        {value === 17 && <CardCustom topic="Tâm sự" />}
      </div>
    </Box>
  );
};

TabCustom.defaultProps = defaultProps;

export default TabCustom;
