import { useEffect, useMemo, useState } from "react";
import {
  DataGrid,
  DataGridProps,
  GridPaginationModel,
} from "@mui/x-data-grid";
import ResizeObserver from "rc-resize-observer";
import { Box, LinearProgress } from "@mui/material";
import { CustomDataGridProps } from "@/types/newpost";
import CustomNoRowsOverlay from "./CustomNoRowOverlay";

export const mapColumnsWithColor = (columns: any) =>
  columns.map((column: any) => ({
    ...column,
    headerClassName: "bg-[#FEF8ED]",
  }));

const CustomDataGrid = (props: CustomDataGridProps & DataGridProps) => {
  const {
    onChangePage,
    onChangeLimit,
    columns,
    autoHeight,
    total,
    limit,
    page,
    rows,
    pageSizeOptions,
    showDensity = true,
    calculateRowsPerPage,
    headerHeight,
    fixedHeight,
    ...rest
  } = props;
  const [density] = useState<"standard" | "compact">("standard");
  const [height, setHeight] = useState<number>(0);
  const [rowCountState, setRowCountState] = useState(total);

  const handlePaginationModelChange = (params: GridPaginationModel) => {
    console.log(params);
    const { pageSize, page: newPage } = params;
    if (newPage + 1 !== page && !rest.loading) {
      onChangePage(newPage + 1);
    }
    if (pageSize !== limit && !rest.loading) {
      onChangeLimit(pageSize);
    }
  };

  const columnsWithColor = useMemo(
    () => mapColumnsWithColor(columns),
    [columns]
  );

  const autoRows = useMemo(
    () => Math.floor((height - 110) / (calculateRowsPerPage || 52)),
    [height, calculateRowsPerPage]
  );

  useEffect(() => {
    setRowCountState((prevRowCountState) =>
      total !== undefined ? total : prevRowCountState
    );
  }, [total, setRowCountState]);

  return (
    <ResizeObserver
      // eslint-disable-next-line no-shadow
      onResize={({ height }) => {
        setHeight(height);
      }}
    >
      <Box
        height={fixedHeight || "1px"}
        flex="1 1 auto"
        sx={{ overflowY: "auto" }}
      >
        <Box sx={{ width: "100%", height: height || "100%" }}>
          {!!height && (
            <DataGrid
              sx={{
                backgroundColor: "white",
                "& .MuiDataGrid-columnHeaderTitle": { fontWeight: 600 },
              }}
              columnHeaderHeight={headerHeight || 44}
              autoHeight={
                autoHeight !== undefined ? autoHeight : rows?.length !== 0
              }
              rows={rows}
              rowCount={rowCountState}
              density={density}
              columns={columnsWithColor}
              paginationModel={{ page: page - 1, pageSize: limit }}
              disableRowSelectionOnClick
              disableDensitySelector={false}
              pageSizeOptions={
                pageSizeOptions || [
                  calculateRowsPerPage ? autoRows : 10,
                  20,
                  50,
                ]
              }
              slots={
                {
                  loadingOverlay: LinearProgress as any,
                  noRowsOverlay: CustomNoRowsOverlay,
                  noResultsOverlay: CustomNoRowsOverlay,
                } as any
              }
              paginationMode="server"
              onPaginationModelChange={handlePaginationModelChange}
              {...rest}
            />
          )}
        </Box>
      </Box>
    </ResizeObserver>
  );
};
export default CustomDataGrid;
