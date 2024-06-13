"use client";
import ActiveUserChart from "@/components/ActiveUserChart";
import General from "@/components/General";
import NewUserChart from "@/components/NewUserChart";
import { Grid } from "@mui/material";
import { useState } from "react";

const ReportChart: React.FC = () => {
  const [totalUser, setTotalUser] = useState(undefined);
  const [totalRegiter, setTotalRegiter] = useState(undefined);
  return (
    <Grid
      container
      spacing={{ xs: 0.5, md: 1 }}
      justifyItems="center"
      sx={{
        "& .MuiButtonBase-root": { display: "none" },
        "& .MuiInputBase-root": { width: "125px" },
      }}
    >
      <Grid item xs={12} md={6} alignItems="center">
        <General title=" TỔNG NGƯỜI ĐĂNG KÝ MỚI" total={totalUser} />
      </Grid>
      <Grid item xs={12} md={6}>
        <General title="TỔNG BÀI VIẾT ĐĂNG MỚI" total={totalRegiter} />
      </Grid>
      <Grid item xs={12} md={6} lg={6}>
        <div
          className="p-4 bg-white rounded-[4px]"
          style={{ overflow: "auto" }}
        >
          <ActiveUserChart
            className="min-w-[500px]"
            title="Người dùng đang hoạt động"
            setTotalUser={(e: any) => setTotalUser(e)}
          />
        </div>
      </Grid>
      <Grid item xs={12} md={6} lg={6}>
        <div
          className="p-4 bg-white rounded-[4px]"
          style={{ overflow: "auto" }}
        >
          <NewUserChart
            className="min-w-[500px]"
            title="Người dùng mới"
            setTotalRegiter={(e: any) => setTotalRegiter(e)}
          />
        </div>
      </Grid>
    </Grid>
  );
};

export default ReportChart;
