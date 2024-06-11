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
import React, { useState, useEffect } from "react";
import PopoverCustom from "./Popover";
import Notification from "./Notification";
import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { usePersonStore } from "@/story";
import ForgetPassword from "./ForgetPassword";
import NewPassword from "./NewPassword";
import { getCookie, setCookie } from "cookies-next";

interface IFormInputs {
  email: string;
  password: string;
}

const schema = yup
  .object({
    email: yup.string().email().required("Trường bắt buộc, vui lòng nhập"),
    password: yup.string().required("Trường bắt buộc, vui lòng nhập"),
  })
  .required();

const Login: React.FC = () => {
  const addUser: any = usePersonStore((state: any) => state.addUser);
  const userData: any = usePersonStore((state: any) => state.user);

  const [dataCookie, setDataCookie] = useState(null);
  const [isDataCookie, setIsDataCookie] = useState<boolean>(false);

  useEffect(() => {
    if (typeof window !== "undefined") {
      const userStorate = localStorage.getItem("user");
      if (userStorate) {
        try {
          setDataCookie(JSON.parse(userStorate));
        } catch (error) {
          console.error(
            "Lỗi khi phân tích dữ liệu JSON từ localStorage:",
            error
          );
          setDataCookie(null);
        }
      }
    }
  }, [isDataCookie]);

  const {
    handleSubmit,
    reset,
    control,
    formState: { errors },
  } = useForm<IFormInputs>({
    defaultValues: {
      email: "",
      password: "",
    },
    resolver: yupResolver(schema),
  });

  const [open, setOpen] = useState(false);
  const [openNoti, setOpenNoti] = useState(false);
  const [message, setMessage] = useState("");
  const [title, setTitle] = useState("Đăng nhập");
  const [showPassword, setShowPassword] = useState(false);
  const [step, setStep] = useState(0);
  const [emailNewPassword, setEmailNewPassword] = useState("");

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setTitle("Đăng nhập");
    setStep(0);
    reset();
  };

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const handleTitle = (title: string) => {
    setTitle(title);
  };

  const onSubmit = async (data: IFormInputs) => {
    if (title === "Đăng nhập") {
      const resp: any = await fetch("http://localhost:8080/user/login", {
        method: "POST",
        mode: "cors",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ ...data }),
      })
        .then((data) => data.json())
        .catch((e) => console.log(e));
      if (resp?.statusCode !== 200) {
        setOpenNoti(true);
        setMessage(resp?.message);
        return;
      }
      if (typeof window !== "undefined") {
        localStorage.setItem("user", JSON.stringify(resp.data));
      }
      setIsDataCookie(true);
      addUser(resp.data);
      setOpenNoti(true);
      setMessage("Đăng nhập thành công");
      handleClose();
    }
    if (title === "Đăng ký") {
      const resp: any = await fetch("http://localhost:8080/user/registry", {
        method: "POST",
        mode: "cors",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ ...data }),
      })
        .then((data) => data.json())
        .catch((e) => console.log(e));
      if (resp?.statusCode !== 200) {
        setOpenNoti(true);
        setMessage(resp?.message);
        return;
      }
      if (typeof window !== "undefined") {
        localStorage.setItem("user", JSON.stringify(resp.data));
      }
      setIsDataCookie(true);
      addUser(resp?.data);
      setOpenNoti(true);
      setMessage("Đăng ký thành công");
      handleClose();
    }
  };

  const handleCloseNoti = () => {
    setOpenNoti(false);
    setMessage("");
  };

  const handleForgetPassword = async () => {
    setStep(1);
    setTitle("Quên mật khẩu");
  };

  const handleChangeNewPassword = (e: string) => {
    setStep(2);
    setTitle("Thay đổi mật khẩu");
    setEmailNewPassword(e);
  };

  const handleBack = () => {
    setStep(0);
    setTitle("Đăng nhập");
  };

  return (
    <div>
      <Notification
        open={openNoti}
        handleCloseNoti={handleCloseNoti}
        message={message}
      />
      {dataCookie ? (
        <PopoverCustom
          setDataCookie={(e: any) => setDataCookie(e)}
          setIsDataCookie={(e) => setIsDataCookie(e)}
        />
      ) : (
        <Button variant="outlined" onClick={handleClickOpen}>
          Đăng nhập
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
          {step === 1 && (
            <ForgetPassword
              handleChangeNewPassword={(e: string) =>
                handleChangeNewPassword(e)
              }
              setOpenNoti={(e: boolean) => setOpenNoti(e)}
              setMessage={(e: string) => setMessage(e)}
            />
          )}
          {step === 2 && (
            <NewPassword
              handleBack={handleBack}
              setOpenNoti={(e: boolean) => setOpenNoti(e)}
              setMessage={(e: string) => setMessage(e)}
              emailNewPassword={emailNewPassword}
            />
          )}
          {step === 0 && (
            <>
              <form onSubmit={handleSubmit(onSubmit)}>
                <Controller
                  name="email"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Email"
                      variant="outlined"
                      className="w-full"
                      autoComplete="off" // Ngăn trình duyệt tự động điền
                    />
                  )}
                />
                <p className="text-[red]">{errors.email?.message}</p>
                <Controller
                  name="password"
                  control={control}
                  render={({ field }) => (
                    <FormControl className="w-full mt-6" variant="outlined">
                      <InputLabel htmlFor="outlined-adornment-passwordss">
                        Password
                      </InputLabel>
                      <OutlinedInput
                        {...field}
                        id="outlined-adornment-passwordss"
                        type={showPassword ? "text" : "password"}
                        endAdornment={
                          <InputAdornment position="end">
                            <IconButton
                              aria-label="toggle password visibility"
                              onClick={handleClickShowPassword}
                              edge="end"
                            >
                              {showPassword ? (
                                <VisibilityOff />
                              ) : (
                                <Visibility />
                              )}
                            </IconButton>
                          </InputAdornment>
                        }
                        label="Password"
                        autoComplete="new-password"
                      />
                    </FormControl>
                  )}
                />
                <p className="text-[red]">{errors.password?.message}</p>
                <div className="mt-5 w-full">
                  <Button
                    variant="contained"
                    className="w-full capitalize"
                    type="submit"
                  >
                    {title}
                  </Button>
                  <div className="mt-3 flex justify-between">
                    <div>
                      <span className="mr-2">Nếu chưa có tài khoản?</span>
                      <a
                        className="text-[#1976d2] decoration-1 cursor-pointer underline"
                        onClick={() =>
                          handleTitle(
                            title === "Đăng nhập" ? "Đăng ký" : "Đăng nhập"
                          )
                        }
                      >
                        {title === "Đăng nhập" ? "Đăng ký" : "Đăng nhập"}
                      </a>
                    </div>
                    <div
                      className="text-[#1976d2] decoration-1 cursor-pointer"
                      onClick={handleForgetPassword}
                    >
                      Quên mật khẩu
                    </div>
                  </div>
                </div>
              </form>
            </>
          )}
        </Box>
      </Modal>
    </div>
  );
};

export default Login;
