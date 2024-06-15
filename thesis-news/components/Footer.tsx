'use client';
import { Divider } from "@mui/joy";
import { usePathname } from "next/navigation";

const Footer = () => {
  const pathname = usePathname();
  return (
    <div className={pathname?.includes('/admin') ? "hidden" : ""}>
      <div className="bg-[#333333] pl-1">
        <div className="pt-10">
          <div>
            <span className="text-white">Giới thiệu</span>
          </div>
        </div>
        <div className="py-6">
          <span className="text-white">
            Thesis News - Trang tin tổng hợp. Áp dụng xử lý ngôn ngữ tự nhiên để tối ưu tìm kiếm của người dùng.
          </span>
        </div>
        <Divider sx={{ marginBottom: "12px", color: "white" }} />
        <div className="pb-10">
          <span className="text-[#d9d5d5]">Source code: </span>
          <span className="text-white"><a href="https://github.com/lqc-repos/5851071007-datn">Github</a> </span>
        </div>
      </div>
    </div>
  );
};

export default Footer;
