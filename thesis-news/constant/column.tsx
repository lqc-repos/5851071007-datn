/* eslint-disable react-hooks/rules-of-hooks */
import { formatDate } from "@/lib/format";
import { MenuItem, Select, SelectChangeEvent, Switch } from "@mui/material";
import { GridColDef } from "@mui/x-data-grid";
import { useState } from "react";
import EditIcon from "@mui/icons-material/Edit";
import DoneIcon from "@mui/icons-material/Done";

const updateUser = async (
  dataLocalStorate: any,
  role: any,
  isActive: boolean,
  id: any,
  type: string
) => {
  const resp = await fetch("http://localhost:8080/user/update", {
    method: "POST",
    mode: "cors",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      memberId: dataLocalStorate.member.id,
      updateMemberId: id,
      roleLevel: role,
      isActive: isActive,
      fullName: dataLocalStorate.member.fullName,
      type: type
    }),
  })
    .then((result) => result.json())
    .catch((e) => console.log(e));

  return resp;
};
const UserColumn = () => {
  const columns: GridColDef[] = [
    {
      field: "index",
      headerName: "STT",
      width: 40,
      disableColumnMenu: true,
    },
    {
      field: "fullName",
      headerName: "Name",
      flex: 1,
      sortable: false,
    },
    {
      field: "email",
      headerName: "Email",
      flex: 1,
      sortable: false,
    },
    {
      field: "createdDate",
      headerName: "Ngày Tạo",
      sortable: false,
      flex: 1,
      renderCell: (data) => formatDate(data.row.createdDate * 1000),
    },
    {
      field: "role",
      headerName: "Quyền",
      flex: 1,
      sortable: false,
      renderCell: (data) => {
        const [isEditRole, setIsEditRole] = useState(false);
        const [role, setRole] = useState(data.row.role);
        const [age, setAge] = useState("");

        const handleChange = (event: SelectChangeEvent) => {
          setAge(event.target.value as string);
        };

        const handleUpdateRole = async () => {
          const dataLocalStorate = JSON.parse(
            localStorage.getItem("user") as any
          );
          const resp: any = await updateUser(
            dataLocalStorate,
            age,
            true,
            data.row.id,
            "role"
          );

          if (resp?.statusCode !== 200) {
          }
          if (resp?.statusCode === 200) {
            setRole(resp?.data?.role?.role);
          }
          setIsEditRole(false);
        };

        return (
          <>
            <div className="block">
              <div className="flex">
                <div className={isEditRole ? "w-full h-10" : ""}>
                  {isEditRole ? (
                    <Select
                      labelId="demo-simple-select-label"
                      id="demo-simple-select"
                      value={age}
                      onChange={handleChange}
                      className="w-full"
                      sx={{
                        height: "40px",
                      }}
                    >
                      <MenuItem value="3">Thành viên</MenuItem>
                      <MenuItem value="2">Tác giả</MenuItem>
                    </Select>
                  ) : (
                    <>
                      {role === "Author" ? (
                        <span>Tác giả</span>
                      ) : (
                        <span>Thành viên</span>
                      )}
                    </>
                  )}
                </div>
                <div>
                  {isEditRole ? (
                    <DoneIcon
                      className="h-[16px] w-[16px] ml-2 cursor-pointer"
                      onClick={handleUpdateRole}
                    />
                  ) : (
                    <EditIcon
                      className="h-[16px] w-[16px] ml-2 cursor-pointer"
                      onClick={() => setIsEditRole(true)}
                    />
                  )}
                </div>
              </div>
            </div>
          </>
        );
      },
    },
    {
      field: "id",
      headerName: "Trạng thái",
      flex: 1,
      sortable: false,
      renderCell: (data) => {
        const label = { inputProps: { "aria-label": "Switch demo" } };
        const [isCheck, setIsCheck] = useState(data.row.isActive);
        const handleChange = async (
          event: React.ChangeEvent<HTMLInputElement>
        ) => {
          const dataLocalStorate = JSON.parse(
            localStorage.getItem("user") as any
          );
          const resp: any = await updateUser(
            dataLocalStorate,
            data.row.roleValue,
            !isCheck,
            data.row.id,
            "status"
          );
          if (resp?.statusCode !== 200) {
          }
          if (resp?.statusCode === 200) {
            setIsCheck(resp?.data?.member?.isActive);
          }
        };
        return (
          <>
            <div className="flex">
              <div>
                <Switch {...label} checked={isCheck} onChange={handleChange} />
              </div>
            </div>
          </>
        );
      },
    },
  ];
  return columns;
};
export default UserColumn;
