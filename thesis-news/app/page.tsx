import DateCustom from "@/components/Date";
import Header from "@/components/Header";
import HightRate from "@/components/HightRate";
import TabCustom from "@/components/Tab";

export default function Home() {
  return (
    <div className="block">
      <Header />
      <div className="max-w-[1336px] m-auto block">
        {/* <div className="justify-evenly flex-row flex my-3">
          <div className="flex-auto flex md:min-w-[1024px] md:max-w-[1024px]">
            <DateCustom />
          </div>
        </div> */}
        <div className="justify-evenly flex-row flex">
          <div className="flex-auto block md:min-w-[728px] md:max-w-[728px]">
            <TabCustom w="inherit" />
          </div>
          <div className="lg:min-w-[368px] lg:max-w-[368px] block min-h-[100vh] border-solid border-[1px] border-[#F2F2F2] pr-6 box-border bg-white em">
            <div className="relative inline-block w-full h-full">
              <div className="sticky mt-0 top-0">
                <div
                  className="flex-col flex"
                  style={{ minHeight: "calc(-57px + 100vh)" }}
                >
                  <HightRate />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
