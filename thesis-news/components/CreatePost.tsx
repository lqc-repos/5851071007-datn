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
import Notification from "./Notification";
import { useState } from "react";

interface IFormInput {
  title: string;
  lable: string;
  description: string;
  content: string;
  topic: string;
}
const CreatePost: React.FC = () => {
  const { control, handleSubmit, reset } = useForm<IFormInput>({
    defaultValues: {
      title: "",
      lable: "",
      description: "",
      content: "",
      topic: "",
    },
  });
  const [openNoti, setOpenNoti] = useState(false);
  const [message, setMessage] = useState("");

  const onSubmit = handleSubmit(async (data) => {
    const memberId = JSON.parse(localStorage.getItem("user") as any);
    const resp = await fetch("http://localhost:8080/user/post", {
      method: "POST",
      mode: "cors",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        ...data,
        label: data.lable.split(","),
        memberId: memberId?.member?.id,
      }),
    })
      .then((result) => result.json())
      .catch((e) => console.log(e));

    if (resp.statusCode !== 200) {
      setOpenNoti(true);
      setMessage(resp?.message);
    }
    if (resp.statusCode === 200) {
      setOpenNoti(true);
      setMessage(resp?.message);
      reset();
    }
  });
  const handleCloseNoti = () => {
    setOpenNoti(false);
    setMessage("");
  };

  return (
    <>
      <Notification
        open={openNoti}
        handleCloseNoti={handleCloseNoti}
        message={message}
      />
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
                    <MenuItem value="Thời sự">Thời sự</MenuItem>
                    <MenuItem value="Góc nhìn">Góc nhìn</MenuItem>
                    <MenuItem value="Thế giới">Thế giới</MenuItem>
                    <MenuItem value="Kinh doanh">Kinh doanh</MenuItem>
                    <MenuItem value="Bất động sản">Bất động sản</MenuItem>
                    <MenuItem value="Khoa học">Khoa học</MenuItem>
                    <MenuItem value="Giải trí">Giải trí</MenuItem>
                    <MenuItem value="Thể thao">Thể thao</MenuItem>
                    <MenuItem value="Pháp luật">Pháp luật</MenuItem>
                    <MenuItem value="Giáo dục">Giáo dục</MenuItem>
                    <MenuItem value="Sức khỏe">Sức khỏe</MenuItem>
                    <MenuItem value="Đời sống">Đời sống</MenuItem>
                    <MenuItem value="Du lịch">Du lịch</MenuItem>
                    <MenuItem value="Số hóa">Số hóa</MenuItem>
                    <MenuItem value="Xe">Xe</MenuItem>
                    <MenuItem value="Ý kiến">Ý kiến</MenuItem>
                    <MenuItem value="Tâm sự">Tâm sự</MenuItem>
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
            <Button
              className="felx items-end"
              variant="outlined"
              onClick={onSubmit}
            >
              Tạo bài viết
            </Button>
          </div>
        </div>
      </div>
    </>
  );
};

export default CreatePost;
