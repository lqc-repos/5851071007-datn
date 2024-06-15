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
            Thesis News - Áp dụng xử lý ngôn ngữ tự nhiên để tăng độ chính xác
            cũng như gợi ý những bài báo có từ khóa mô tả của người dùng
          </span>
        </div>
        <Divider sx={{ marginBottom: "12px", color: "white" }} />
        <div className="pb-10">
          <span className="text-[#d9d5d5]">Source code: </span>
          <span className="text-white">Github </span>
        </div>
      </div>
    </div>
  );
};

export default Footer;
