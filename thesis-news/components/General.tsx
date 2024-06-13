interface GeneralProps {
  title?: string;
  total?: number;
}
// eslint-disable-next-line arrow-body-style
const General = ({ title = "", total = 0 }: GeneralProps) => {
  return (
    <div className="flex items-center p-4 bg-white rounded-[4px] font-[500] text-[14px]">
      <span className="flex-1">{title}</span>
      <span className="text-[24px] text-primary float-right items-end">
        {total.toLocaleString()}
      </span>
    </div>
  );
};

export default General;
