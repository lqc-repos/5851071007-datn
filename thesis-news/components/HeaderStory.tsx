import Image from "next/image";
import Login from "./Login";
import { Container } from "@mui/material";
import Link from "next/link";

const HeaderStory: React.FC = () => {
  return (
    <Container maxWidth="lg">
      <div className="block static top-0 z-50">
        <div className="hidden h-[41px] leading-5 p-0" />
        <div className="h-[57px] flex items-center px-6">
          <div className="flex items-center flex-auto">
            <Link href="/">
              <Image
                src="https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg"
                alt="logo"
                width={20}
                height={20}
              />
            </Link>
          </div>
          <Login />
        </div>
      </div>
    </Container>
  );
};

export default HeaderStory;
