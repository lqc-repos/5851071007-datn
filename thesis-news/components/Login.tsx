/* eslint-disable react/no-unescaped-entities */
"use client";
import { STYLE_LOGIN } from "@/constant";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { useForm, Controller } from "react-hook-form";
import {
  Box,
  Button,
  FormControl,
  IconButton,
  InputAdornment,
  InputLabel,
  Modal,
  OutlinedInput,
  TextField,
} from "@mui/material";
import React from "react";
import PopoverCustom from "./Popover";
import Notification from "./Notification";
import { useRouter } from "next/navigation";
import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";

interface IFormInputs {
  email: string;
  password: string;
}

const schema = yup
  .object({
    email: yup.string().email().required("Trường bắt buộc, vui long nhập"),
    password: yup.string().required("Trường bắt buộc, vui long nhập"),
  })
  .required();

const Login: React.FC = () => {
  const router = useRouter();
  const {
    handleSubmit,
    control,
    formState: { errors },
  } = useForm<IFormInputs>({
    defaultValues: {
      email: "",
      password: "",
    },
    resolver: yupResolver(schema),
  });
  const [open, setOpen] = React.useState(false);
  const [isLogin, setIsLogin] = React.useState(false);
  const [openNoti, setOpenNoti] = React.useState(false);
  const [message, setMessage] = React.useState("");
  const [title, setTitle] = React.useState("Đăng nhập");
  const [showPassword, setShowPassword] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const handleTitle = (title: string) => {
    setTitle(title);
  };

  const onSubmit = handleSubmit(async (data) => {
    if (title === "Đăng nhập") {
      const resp = await fetch("http://localhost:8080/user/login", {
        method: "POST",
        mode: "cors",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ ...data })
      });
      console.log(resp);
    }
    if (title === "Đăng ký") {
      const resp = await fetch("http://localhost:8080/user/registry", {
        method: "POST",
        mode: "cors",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ ...data })
      });
      console.log(resp);
    }
  });

  const handleCloseNoti = () => {
    setOpenNoti(false);
    setMessage("");
  };
  return (
    <div>
      <Notification
        open={openNoti}
        handleCloseNoti={handleCloseNoti}
        message={message}
      />
      {isLogin ? (
        <PopoverCustom />
      ) : (
        <Button variant="outlined" onClick={handleClickOpen}>
          Login
        </Button>
      )}
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={STYLE_LOGIN}>
          <h1 className="mb-10 text-center text-3xl font-medium">{title}</h1>
          <Controller
            name="email"
            control={control}
            render={({ field }) => (
              <TextField
                label="Email"
                variant="outlined"
                className="w-full"
                autoComplete="no"
                {...field}
              />
            )}
          />
          <p className="text-[red]">{errors.email?.message}</p>
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
          <p className="text-[red]">{errors.email?.message}</p>
          <div className="mt-5 w-full">
            <Button
              variant="contained"
              className="w-full capitalize"
              onClick={onSubmit}
            >
              {title}
            </Button>
            <div className="mt-3">
              <span className="mr-2">Don't have an account?</span>
              <a
                className="text-[#1976d2] decoration-1 cursor-pointer underline"
                onClick={() =>
                  handleTitle(title === "Đăng nhập" ? "Đăng ký" : "Đăng nhập")
                }
              >
                {title === "Đăng nhập" ? "Đăng ký" : "Đăng nhập"}
              </a>
            </div>
          </div>
        </Box>
      </Modal>
    </div>
  );
};

export default Login;
