import React, { useState } from "react";
import {
  Box,
  CircularProgress,
  IconButton,
  InputBase,
  Pagination,
  Paper,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import { useForm, Controller } from "react-hook-form";
import CardIndex from "./CardIndex";

const Search: React.FC = () => {
  const { control, handleSubmit } = useForm();
  const [data, setData] = useState([]);
  const [value, setValue] = useState<any>();
  const [page, setPage] = useState(1);
  const [isLoading, setIsLoading] = useState(false);
  const [totalData, setTotalData] = useState(1);
  const memberId = JSON.parse(localStorage.getItem("user") as any);

  const getData = async (value: any, currentPage: number) => {
    try {
      const dataSearch = {
        search: value?.search,
        size: 10,
        page: currentPage,
        memberId: memberId?.member?.id,
      };
      const resp = await fetch(`http://localhost:8080/article/search`, {
        method: "POST",
        mode: "cors",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(dataSearch),
      });
      if (!resp.ok) {
        throw new Error("Failed to fetch data");
      }
      const responseData = await resp.json();
      setData(responseData?.data?.articles || []);
      setTotalData(responseData?.data?.totalPage || 1);
    } catch (error) {
      console.error("Error fetching data:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const onSubmit = handleSubmit((formData) => {
    if (!formData.search) return;
    setIsLoading(true);
    setValue(formData);
    setPage(1); // Reset page to 1 when submitting new search
    getData(formData, 1); // Fetch data for the first page
  });

  const handleKeyDown = (event: React.KeyboardEvent) => {
    if (event.key === "Enter") {
      event.preventDefault();
      onSubmit();
    }
  };

  const handleChange = (_event: React.ChangeEvent<unknown>, valuePage: number) => {
    setPage(valuePage); // Update page state
    getData(value, valuePage); // Fetch data for the selected page
  };

  return (
    <div>
      <Paper
        component="form"
        sx={{
          p: "2px 4px",
          display: "flex",
          alignItems: "center",
          width: 300,
          marginLeft: "14px",
          boxShadow: "none",
          border: "1px solid rgba(202, 198, 198, 0.5)",
          borderRadius: "20px",
          height: "42px",
          marginTop: "16px",
        }}
      >
        <Controller
          name="search"
          control={control}
          render={({ field }) => (
            <InputBase
              sx={{ ml: 1, flex: 1 }}
              placeholder="Search"
              inputProps={{ "aria-label": "search google maps" }}
              {...field}
              onKeyDown={handleKeyDown}
            />
          )}
        />
        <IconButton
          type="button"
          sx={{ p: "10px" }}
          aria-label="search"
          onClick={onSubmit}
        >
          <SearchIcon />
        </IconButton>
      </Paper>
      {isLoading ? (
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            position: "relative",
            top: "50px",
          }}
        >
          <CircularProgress />
        </Box>
      ) : (
        <>
          <CardIndex data={data} />
          {data.length > 0 && (
            <div className="block justify-end">
              <Pagination
                count={totalData}
                variant="outlined"
                shape="rounded"
                className="block justify-end"
                color="primary"
                page={page}
                onChange={handleChange}
              />
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default Search;
