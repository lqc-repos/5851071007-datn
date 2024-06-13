/* eslint-disable react-hooks/rules-of-hooks */
import { formatDate } from "@/lib/format";
import { MenuItem, Select, SelectChangeEvent, Switch } from "@mui/material";
import { GridColDef } from "@mui/x-data-grid";
import { useState } from "react";
import EditIcon from "@mui/icons-material/Edit";
import DoneIcon from "@mui/icons-material/Done";

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
    // {
    //     field: 'createdDate',
    //     headerName: 'Email',
    //     flex: 1,
    //     sortable: false,
    // },
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
      renderCell: () => {
        const [isEditRole, setIsEditRole] = useState(false);
        const [age, setAge] = useState("");
        const handleChange = (event: SelectChangeEvent) => {
          setAge(event.target.value as string);
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
                      <MenuItem value="member">Thành viên</MenuItem>
                      <MenuItem value="author">Người đăng bài</MenuItem>
                    </Select>
                  ) : (
                    <span>Người dùng</span>
                  )}
                </div>
                <div>
                  {isEditRole ? (
                    <DoneIcon
                      className="h-[16px] w-[16px] ml-2"
                      onClick={() => setIsEditRole(false)}
                    />
                  ) : (
                    <EditIcon
                      className="h-[16px] w-[16px] ml-2"
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
      field: "isActive",
      headerName: "Trạng thái",
      flex: 1,
      sortable: false,
      minWidth: 120,
      maxWidth: 120,
      renderCell: (data) => {
        const label = { inputProps: { "aria-label": "Switch demo" } };
        return <Switch {...label} disabled checked={data.row.isActive} />;
      },
    },
    {
      field: "id",
      headerName: "Thao tác",
      flex: 1,
      sortable: false,
      renderCell: (data) => {
        const label = { inputProps: { "aria-label": "Switch demo" } };
        return (
          <>
            <div className="flex">
              <div>
                <span>Khóa tài khoản: </span>
              </div>
              <div>
                <Switch {...label} checked={data.row.isDeleted} />
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
