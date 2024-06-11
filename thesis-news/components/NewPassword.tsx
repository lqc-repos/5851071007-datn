import {
  Button,
  FormControl,
  IconButton,
  InputAdornment,
  InputLabel,
  OutlinedInput,
} from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import React from "react";

interface IFormInputs {
  password: string;
}

const schema = yup
  .object({
    password: yup.string().required("Trường bắt buộc, vui long nhập"),
  })
  .required();

const NewPassword: React.FC<{
  handleBack: () => void;
  setOpenNoti: (e: boolean) => void;
  setMessage: (e: string) => void;
  emailNewPassword: string;
}> = ({ handleBack, setMessage, setOpenNoti, emailNewPassword }) => {
  const {
    handleSubmit,
    control,
    formState: { errors },
  } = useForm<IFormInputs>({
    defaultValues: {
      password: "",
    },
    resolver: yupResolver(schema),
  });
  const [showPassword, setShowPassword] = React.useState(false);

  const onSubmit = async (data: IFormInputs) => {
    const resp = await fetch("http://localhost:8080/user/reset-password", {
      method: "POST",
      mode: "cors",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: emailNewPassword,
        password: data.password,
      }),
    })
      .then((result) => result.json())
      .catch((e) => console.log(e));

    if (resp.statusCode !== 200) {
      setOpenNoti(true);
      setMessage(resp?.message || resp?.data?.message);
      return;
    }
    setOpenNoti(true);
    setMessage(resp?.message || resp?.data?.message);
    handleBack();
  };

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <Controller
          name="password"
          control={control}
          render={({ field }) => (
            <FormControl className="w-full mt-6" variant="outlined">
              <InputLabel htmlFor="outlined-adornment-password">
                Password
              </InputLabel>
              <OutlinedInput
                id="outlined-adornment-password"
                type={showPassword ? "text" : "password"}
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={handleClickShowPassword}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                }
                label="Password"
                {...field}
              />
            </FormControl>
          )}
        />
        <p className="text-[red]">{errors.password?.message}</p>
      </div>
      <div className="mt-5 w-full">
        <Button
          variant="contained"
          className="w-full capitalize"
          // onClick={onSubmit}
          type="submit"
        >
          Xác nhận
        </Button>
      </div>
    </form>
  );
};

export default NewPassword;
