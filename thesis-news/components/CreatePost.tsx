import { Textarea } from "@mui/joy";
import {
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { useForm, Controller, SubmitHandler } from "react-hook-form";

interface IFormInput {
  title: string;
  lable: string;
  description: string;
  content: string;
  topic: string;
}
const CreatePost: React.FC = () => {
  const { control, handleSubmit } = useForm<IFormInput>({
    defaultValues: {
      title: "",
      lable: "",
      description: "",
      content: "",
      topic: "",
    },
  });
  const onSubmit: SubmitHandler<IFormInput> = (data) => {
    console.log(data);
  };
  return (
    <>
      <div className="block">
        <div className="flex flex-col">
          <div className="mb-4">
            <h1 className="font-bold text-2xl">Tạo bài viết mới</h1>
          </div>
          <div className="flex flex-col">
            <Controller
              name="title"
              control={control}
              render={({ field }) => (
                <TextField
                  label="Tiêu đề"
                  variant="outlined"
                  className="w-full mb-5"
                  autoComplete="no"
                  {...field}
                />
              )}
            />
            <Controller
              name="lable"
              control={control}
              render={({ field }) => (
                <TextField
                  label="Từ khóa"
                  variant="outlined"
                  className="w-full mb-5"
                  autoComplete="no"
                  {...field}
                />
              )}
            />
            <Controller
              name="description"
              control={control}
              render={({ field }) => (
                <TextField
                  label="Mô tả"
                  variant="outlined"
                  className="w-full mb-5"
                  autoComplete="no"
                  {...field}
                />
              )}
            />
            <Controller
              name="topic"
              control={control}
              render={({ field }) => (
                <FormControl fullWidth>
                  <InputLabel id="demo-simple-select-label">Chủ đề</InputLabel>
                  <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    className="w-full mb-5"
                    label="Chủ đề"
                    {...field}
                  >
                    <MenuItem value="thoi-su">Thời sự</MenuItem>
                    <MenuItem value="goc-nhin">Góc nhìn</MenuItem>
                    <MenuItem value="the-gioi">Thế giới</MenuItem>
                    <MenuItem value="kinh-doanh">Kinh doanh</MenuItem>
                    <MenuItem value="bat-dong-san">Bất động sản</MenuItem>
                    <MenuItem value="khoa-hoc">Khoa học</MenuItem>
                    <MenuItem value="giai-tri">Giải trí</MenuItem>
                    <MenuItem value="the-thao">Thể thao</MenuItem>
                    <MenuItem value="phap-luat">Pháp luật</MenuItem>
                    <MenuItem value="giao-duc">Giáo dục</MenuItem>
                    <MenuItem value="suc-khoe">Sức khỏe</MenuItem>
                    <MenuItem value="doi-song">Đời sống</MenuItem>
                    <MenuItem value="du-lich">Du lịch</MenuItem>
                    <MenuItem value="so-hoa">Số hóa</MenuItem>
                    <MenuItem value="xe">Xe</MenuItem>
                    <MenuItem value="y-kien">Ý kiến</MenuItem>
                    <MenuItem value="tam-su">Tâm sự</MenuItem>
                  </Select>
                </FormControl>
              )}
            />
            <Controller
              name="content"
              control={control}
              render={({ field }) => (
                <Textarea
                  size="md"
                  placeholder="Nội dung"
                  {...field}
                  minRows={3}
                />
              )}
            />
          </div>
          <div className="mt-5">
            <Button className="felx items-end" variant="outlined">
              Tạo bài viết
            </Button>
          </div>
        </div>
      </div>
    </>
  );
};

export default CreatePost;
