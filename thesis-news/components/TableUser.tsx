import React, { useMemo } from "react";
import { OrderTableProps } from "@/types/newpost";
import { UseTableContext } from "./TableProvider";
import CustomDataGrid from "./CustomDataGrid";
import UserColumn from "@/constant/column";

const OrderTable = (props: OrderTableProps) => {
  const { onChangePage, onChangeLimit } = props;
  const { data: rows, total, limit, page, loading } = UseTableContext();
  const formattedRows = useMemo(
    () =>
      rows.map((row, index) => {
        return {
          ...row,
          index: index + 1,
        };
      }),
    [rows]
  );

  return (
    <CustomDataGrid
      columns={UserColumn()}
      rows={formattedRows}
      loading={loading}
      page={page}
      limit={limit}
      total={total}
      onChangePage={onChangePage}
      onChangeLimit={onChangeLimit}
    />
  );
};

export default OrderTable;
