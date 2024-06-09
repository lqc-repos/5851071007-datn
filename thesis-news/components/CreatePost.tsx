import { Textarea } from "@mui/joy";
import { TextField } from "@mui/material";
import { useForm, Controller, SubmitHandler } from "react-hook-form";

interface IFormInput {
  title: string;
  lable: string;
  description: string;
  content: string;
}
const CreatePost: React.FC = () => {
  const { control, handleSubmit } = useForm({
    defaultValues: {
      title: "",
      lable: "",
      description: "",
      content: ""
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
                  label="Title"
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
                  label="Lable"
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
        </div>
      </div>
    </>
  );
};

export default CreatePost;
