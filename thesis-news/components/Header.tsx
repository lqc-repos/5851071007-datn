import Image from "next/image";
import Login from "./Login";
import Link from "next/link";

const Header: React.FC = () => {
  return (
    <div className="block static top-0 z-50">
      <div className="hidden h-[41px] leading-5 p-0 border-b-[#F2F2F2] border-[1px]" />
      <div className="h-[57px] flex items-center px-6 border-b-[#F2F2F2] border-[1px]">
        <div className="flex items-center flex-auto">
          <Link href="/" className="flex items-center flex-auto">
            <Image
              src="https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg"
              alt="logo"
              width={20}
              height={20}
            />
            <span className="font-bold text-[red]">Thesis News</span>
          </Link>
        </div>
        <Login />
      </div>
    </div>
  );
};

export default Header;
