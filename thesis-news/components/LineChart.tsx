import React from 'react';
import { Line } from 'react-chartjs-2';
import {
  CategoryScale,
  Chart as ChartJS,
  ChartData,
  ChartTypeRegistry,
  Legend,
  LinearScale,
  LineElement,
  PointElement,
  Title,
  Tooltip,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
);

export const defaultOptions = {
  responsive: true,
  plugins: {
    legend: { position: 'bottom' as const },
    title: { display: false, text: '' },
  },
  scales: {
    y: { beginAtZero: true },
  },
};

export interface LineChartProps {
    options?: ChartTypeRegistry,
    data?: ChartData,
}

const LineChart = ({ options, data } : LineChartProps) => {
  const mapOptions = options ? { ...defaultOptions, ...options } : defaultOptions;
  return (
    <Line redraw options={mapOptions} data={data as any} />
  );
};

export default LineChart;
