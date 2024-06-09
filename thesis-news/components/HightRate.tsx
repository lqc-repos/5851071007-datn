import Link from "next/link";

const HightRate: React.FC = () => {
  return (
    <div className="flex-auto block">
      <div className="mt-10 block">
        <div className="mb-5 block">Topic rate</div>
        <div className="pb-5 block">
          <div className="w-full h-full">
            <div className="mb-2 block text-xs gg gf ge gd">
              Torsten Walbaum in Towards Data Science
            </div>
            <div className="block">
              <Link href="/">
                <h2 className="font-bold tracking-normal text-base leading-5">
                  What 10 Years at Uber, Meta and Startups Taught Me About Data
                  Analytics
                </h2>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HightRate;
