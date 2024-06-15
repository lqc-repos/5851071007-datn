"use client";
import { Avatar } from "@mui/material";
import MenuList from "@mui/material/MenuList";
import MenuItem from "@mui/material/MenuItem";
import ListItemText from "@mui/material/ListItemText";
import React, { useEffect, useState } from "react";
import { Divider } from "@mui/joy";
import Infomation from "./Infomation";
import ArticleSave from "./ArticleSave";
import ArticleSee from "./AricleSee";
import CreatePost from "./CreatePost";
import AriclePushlish from "./AriclePushlish";

const NewPost: React.FC = () => {
  const [dataCookie, setDataCookie] = useState<any>(null);
  const [value, setValue] = useState(0);
  const handleClick = (val: number) => {
    setValue(val);
  };
  useEffect(() => {
    if (typeof window !== "undefined") {
      const userStorate = localStorage.getItem("user");
      if (userStorate) {
        try {
          setDataCookie(JSON.parse(userStorate));
        } catch (error) {
          console.error(
            "Lỗi khi phân tích dữ liệu JSON từ localStorage:",
            error
          );
          setDataCookie(null);
        }
      }
    }
  }, []);
  console.log(dataCookie);
  return (
    <div>
      <div className="max-w-[1336px] m-auto block mt-10">
        <div className="justify-evenly flex-row flex">
          <div className="flex-auto block md:min-w-[328px] md:max-w-[328px]">
            <div className="border-solid border-[1px] border-[#bdbdbd] rounded-lg">
              <div className="flex items-center flex-col py-5">
                <Avatar sx={{ width: 80, height: 80 }}>
                  {dataCookie && dataCookie?.member?.fullName.charAt(0).toUpperCase()}
                </Avatar>
                {dataCookie && dataCookie?.role?.role === "Admin" && (
                  <span>Quản trị</span>
                )}
                {dataCookie && dataCookie?.role?.role === "Author" && (
                  <span>Tác giả</span>
                )}
                {dataCookie && dataCookie?.role?.role === "Member" && (
                  <span>Thành viên</span>
                )}
                <span></span>
              </div>
              <Divider component="li" />
              <div>
                <MenuList>
                  <MenuItem onClick={() => handleClick(0)}>
                    <ListItemText>Thông tin chung</ListItemText>
                  </MenuItem>
                  {dataCookie && dataCookie?.role?.role !== "Member" && (
                    <MenuItem onClick={() => handleClick(1)}>
                      <ListItemText>Tạo bài viết mới</ListItemText>
                    </MenuItem>
                  )}
                  <MenuItem onClick={() => handleClick(2)}>
                    <ListItemText>Bài viết đã lưu</ListItemText>
                  </MenuItem>
                  <MenuItem onClick={() => handleClick(3)}>
                    <ListItemText>Bài viết đã xem</ListItemText>
                  </MenuItem>
                  {dataCookie && dataCookie?.role?.role !== "Member" && (
                    <MenuItem onClick={() => handleClick(4)}>
                      <ListItemText>Bài viết đã tạo</ListItemText>
                    </MenuItem>
                  )}
                </MenuList>
              </div>
            </div>
          </div>
          <Divider orientation="vertical" />
          <div className="lg:min-w-[728px] lg:max-w-[728px] block min-h-[100vh] pr-6 box-border bg-white em !pl-0">
            <div className="relative inline-block w-full h-full">
              <div className="sticky mt-0 top-0">
                {value === 0 && (
                  <div
                    className="flex-col flex"
                    style={{ minHeight: "calc(-57px + 100vh)" }}
                  >
                    <Infomation
                      name={dataCookie && dataCookie?.member?.fullName}
                      email={dataCookie && dataCookie?.account?.email}
                    />
                  </div>
                )}
                {value === 1 && (
                  <div
                    className="flex-col flex"
                    style={{ minHeight: "calc(-57px + 100vh)" }}
                  >
                    <CreatePost />
                  </div>
                )}
                {value === 2 && (
                  <div
                    className="flex-col flex"
                    style={{ minHeight: "calc(-57px + 100vh)" }}
                  >
                    <ArticleSave />
                  </div>
                )}
                {value === 3 && (
                  <div
                    className="flex-col flex"
                    style={{ minHeight: "calc(-57px + 100vh)" }}
                  >
                    <ArticleSee />
                  </div>
                )}
                {value === 4 && (
                  <div
                    className="flex-col flex"
                    style={{ minHeight: "calc(-57px + 100vh)" }}
                  >
                    <AriclePushlish />
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NewPost;
