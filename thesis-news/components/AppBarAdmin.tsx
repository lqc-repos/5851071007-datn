"use client"
import {
  Breadcrumbs,
  IconButton,
  Toolbar,
  Typography,
  styled,
} from "@mui/material";
import AccessTimeFilledIcon from "@mui/icons-material/AccessTimeFilled";
import Link from "next/link";
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import { MenuItemProps } from "@/types/newpost";
import { useMemo } from "react";
import { MenuTypes, Menus } from "@/constant";
import { useRouter } from "next/navigation";

const drawerWidth = 200;

const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== "open",
})<any>(({ theme, open, ismobile }) => ({
  zIndex: theme.zIndex.drawer + (ismobile === "false" ? 1 : 0),
  transition: theme.transitions.create(["width", "margin"], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  "& .MuiToolbar-root": {
    minHeight: "46px",
    paddingLeft: "16px",
    paddingRight: "16px",
  },
  ...(open &&
    ismobile === "false" && {
      marginLeft: drawerWidth,
      width: `calc(100% - ${drawerWidth}px)`,
      transition: theme.transitions.create(["width", "margin"], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
      }),
    }),
}));

export default function AppBarAdmin({
  ismobile = false,
  open,
  toggleDrawer = () => {},
}: any) {
  const router = useRouter();

  const convertMenus = useMemo(() => {
    let result: MenuItemProps[] = [];
    // eslint-disable-next-line no-restricted-syntax
    for (const menu of Menus) {
      if (menu.type === MenuTypes.ACCORDION) {
        result = result.concat(menu.childrens as any);
      } else {
        result.push(menu);
      }
    }
    return result;
  }, [Menus]);

  if (!convertMenus.length) return <></>;

//   const routerParent = convertMenus.find(
//     (m) => router?.pathname?.search(m.path) > -1
//   );
//   const routerChild = routerParent
//     ? (routerParent?.childrens || []).find((m) => router?.pathname === m.path)
//     : null;

//   if (!routerParent) return <></>;
//   const { Icon } = routerParent;

  return (
    <AppBar
      position="absolute"
      open={open}
      ismobile={ismobile.toString()}
      sx={{ height: "46px" }}
    >
      <Toolbar>
        <IconButton
          edge="start"
          color="inherit"
          onClick={toggleDrawer}
          sx={{
            marginRight: "24px",
            ...(open && { display: "none" }),
          }}
        >
          <AccessTimeFilledIcon width={20} height={20} />
        </IconButton>

        {/* <Icon width="18" height="18" fill="white" /> */}
        <Breadcrumbs
          aria-label="breadcrumb"
          className="ml-2 text-white"
          sx={{ flexGrow: 1 }}
        >
          {/* {routerChild ? (
            <Typography className="opacity-80">
              <Link href={routerParent.path}>{routerParent.label}</Link>
            </Typography>
          ) : (
            <Typography color="white">{routerParent?.label}</Typography>
          )} */}

          {/* {routerChild && (
            <Typography className="text-white">{routerChild?.label}</Typography>
          )} */}
        </Breadcrumbs>
      </Toolbar>
    </AppBar>
  );
}
