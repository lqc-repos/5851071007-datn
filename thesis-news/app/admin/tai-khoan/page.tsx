"use client";
import TableProvider, { TableHandle } from "@/components/TableProvider";
import OrderTable from "@/components/TableUser";
import { Box, IconButton, InputBase, Paper } from "@mui/material";
import { useEffect, useRef, useState } from "react";
import { Controller, useForm } from "react-hook-form";
import SearchIcon from "@mui/icons-material/Search";
import { useSearchParams, useRouter } from "next/navigation";
import { handleGetListUser } from "@/action";

const AdminAccount: React.FC = () => {
  const searchParams = useSearchParams();
  const router = useRouter();
  const { control, setValue, getValues } = useForm({
    defaultValues: {
      search: "",
    },
  });

  const tableRef = useRef<TableHandle>(null);
  const page = Number(searchParams?.get("list_page"))
    ? Number(searchParams?.get("list_page"))
    : 1;
  const limit = Number(searchParams?.get("list_limit"))
    ? Number(searchParams?.get("list_limit"))
    : 10;

  const handleGetData = () => {
    const memberId =
      JSON.parse(localStorage.getItem("user") as any)?.member?.id || "";
    handleGetListUser("", page, limit, memberId, tableRef);
  };

  const handleChangePage = (newPage: number) => {
    console.log(newPage);
    if (page === newPage) return;
    router.push(`/admin/tai-khoan/?list_limit=${limit}&list_page=${newPage}`);
  };

  const handleChangeLimit = (limit: number) => {
    router.push(`/admin/tai-khoan/?list_limit=${limit}&list_page=${page}`);
  };

  useEffect(() => {
    handleGetData();
  }, [page, limit]);

  return (
    <div className="flex flex-col h-full">
      <div className="flex gap-2 overflow-x-auto">
        <div className="max-w-[500px] min-w-[150px] w-full">
          <Controller
            name="search"
            control={control}
            render={({ field }) => (
              <Paper
                component="form"
                sx={{
                  display: "flex",
                  alignItems: "center",
                  marginBottom: "4px",
                  width: 400,
                  boxShadow: "none",
                  border: "1px solid rgba(202, 198, 198, 0.5)",
                  borderRadius: "8px",
                  height: "42px",
                }}
              >
                <InputBase
                  sx={{ ml: 1, flex: 1 }}
                  placeholder="Search"
                  inputProps={{ "aria-label": "search google maps" }}
                  {...field}
                />
                <IconButton
                  type="button"
                  sx={{ p: "10px" }}
                  aria-label="search"
                  // onClick={onSubmit}
                >
                  <SearchIcon />
                </IconButton>
              </Paper>
            )}
          />
        </div>
      </div>
      <Box
        height="1px"
        flex="1 1 auto"
        display="flex"
        flexDirection="column"
        sx={{ overflowY: "auto" }}
      >
        <TableProvider ref={tableRef}>
          <OrderTable
            onChangePage={handleChangePage}
            onChangeLimit={handleChangeLimit}
          />
        </TableProvider>
      </Box>
    </div>
  );
};

export default AdminAccount;
