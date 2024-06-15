"use client";
/* eslint-disable react/no-unused-prop-types */
// eslint-disable-next-line object-curly-newline
import { useEffect, useState } from "react";
import moment from "moment";
import { rangeDateByDays } from "@/constant";
import { UserChartProps } from "@/types/newpost";
import { TableHandle } from "./TableProvider";
import HeaderChart, { showDataLabels } from "./HeaderChart";
import LineChart from "./LineChart";
import { adminNotify, notifyType } from "@/lib/format";

const options: any = {
  scales: {
    y: {
      beginAtZero: true,
      ticks: { stepSize: 1 },
    },
  },
};

const datasets: any = [
  {
    id: 1,
    label: "Người đăng kí mới",
    data: [0],
    borderColor: "rgb(53, 162, 235)",
    backgroundColor: "rgba(53, 162, 235, 0.5)",
  },
];

const formatDate = "MM/DD/YYYY";
const formatDateRequest = "DD/MM/YYYY";
const tracking_type = "SIGN_IN" as any;
const group_by = "DAY" as any;
const defaultDays = 30;
const toDate = moment().format(formatDate);
const fromDate = moment(toDate).add(-defaultDays, "days").format(formatDate);
const dataLabels = showDataLabels(fromDate, toDate);

const ActiveUserChart = ({ className = "", setTotalUser }: UserChartProps) => {
  const [rangeDate, setDateRange] = useState<[string, string]>([
    fromDate,
    toDate,
  ]);
  const [dataChart, setDataChart] = useState<any>({
    labels: dataLabels,
    datasets: [],
  });

  const [openModal, setOpenModal] = useState(false);
  // eslint-disable-next-line object-curly-newline
  const queries = {
    limit: -1,
    page: 1,
    tracking_type,
    from_date: "",
    to_date: "",
    group_by,
  };

  const handleChangeDateRange = async (rangeValues: [string, string]) => {
    dataChart.datasets = datasets;
    const fromDate = moment(rangeValues[0]).format("MM/DD/YYYY");
    const toDate = moment(rangeValues[1]).format("MM/DD/YYYY");
    const newToDate = moment(rangeValues[1]).format(formatDateRequest);
    const newDataLabels = showDataLabels(rangeValues[0], rangeValues[1]);
    const mapDataLabels = rangeDateByDays(
      rangeValues[0],
      rangeValues[1],
      formatDateRequest
    );

    const dataTracking = await fetch("http://localhost:8080/report/get", {
      method: "POST",
      mode: "cors",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        reportType: "registry",
        fromDate: moment(fromDate, "MM/DD/YYYY").valueOf() / 1000,
        toDate: moment(toDate, "MM/DD/YYYY").valueOf() / 1000,
      }),
    })
      .then((result) => result.json())
      .catch((e) => console.log(e));

    if (dataTracking?.statusCode !== 200) {
      adminNotify(dataTracking?.message, notifyType.ERROR);
      setDataChart({
        labels: newDataLabels,
        datasets: [
          {
            ...datasets[0],
            data: Array(newDataLabels.length).fill(0),
          },
        ],
      });
      return;
    }
    if (dataTracking && dataTracking?.data?.reports?.length > 0) {
      const dataset = mapDataLabels.map((label) => {
        const isExisted = dataTracking?.data?.reports?.find(
          (tracking: any) => tracking.key === label
        );
        return isExisted?.value || 0;
      });
      dataChart.labels = newDataLabels;
      dataChart.datasets[0].data = dataset;
      setDataChart({ ...dataChart });
      if (setTotalUser) setTotalUser(dataTracking.data.totalValue);
    }

    setDateRange([rangeValues[0], newToDate]);
  };
  const handleModal = (open: boolean) => {
    if (open) {
      queries.from_date = moment(rangeDate[0]).startOf("date").format();
      queries.to_date = moment(rangeDate[1]).endOf("date").format();
    } else {
      setOpenModal(open);
    }
  };

  useEffect(() => {
    handleChangeDateRange(rangeDate);
  }, []);

  return (
    <div className={className}>
      <HeaderChart
        defaultDays={defaultDays}
        onChangeView={() => handleModal(!openModal)}
        onChangeDateRange={(value: [string, string]) =>
          handleChangeDateRange(value)
        }
      />
      <LineChart data={dataChart} options={options} />
    </div>
  );
};

export default ActiveUserChart;
