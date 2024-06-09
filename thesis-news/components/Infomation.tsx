import EditIcon from "@mui/icons-material/Edit";
import { Divider } from "@mui/joy";

const Infomation: React.FC<{ name: string }> = ({ name }) => {
  return (
    <>
      <div className="block">
        <div className="flex flex-col">
          <div className="mb-4">
            <h1 className="font-bold text-2xl">Thông tin tài khoản</h1>
          </div>
          {name && (
            <div className="flex flex-col">
              <div className="flex justify-between">
                <label className="font-semibold pb-2">Tên: </label>
              </div>
              <span>{name || ""}</span>
            </div>
          )}
          <div className="flex flex-col">
            <div className="flex justify-between">
              <label className="font-semibold pb-2">Email: </label>
            </div>
            <span>tvietnguyen1997@gmail.com</span>
          </div>
          <Divider component="li" className="my-4" />
          <div className="flex flex-col">
            <div className="flex justify-between">
              <label className="font-semibold pb-2">passwordl: </label>
              <label className="cursor-pointer">
                <EditIcon />
              </label>
            </div>
            <span>******</span>
          </div>
          <Divider component="li" className="my-4" />
        </div>
      </div>
    </>
  );
};

export default Infomation;
