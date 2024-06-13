"use client";
/* eslint-disable no-restricted-syntax */
import { useMemo } from "react";
import LogoutIcon from "@mui/icons-material/Logout";
import {
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
} from "@mui/material";
import Divider from "@mui/material/Divider";
import MuiDrawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import { styled } from "@mui/material/styles";
import { EntityRoleAssign, MenuItemProps } from "@/types/newpost";
import { HeightDefault, MenuTypes, Menus } from "@/constant";
import MenuItem from "./MenuItem";
import Link from "next/link";
import Image from "next/image";
// import { CircaLogoIcon } from '@public/assets/icons';

// import { HeightDefault as heightDefault, Menus, MenuTypes } from './utils/constant';
// import MenuItem, { MenuItemProps } from './MenuItem';

const drawerWidth = 200;
const Drawer = styled(MuiDrawer, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme, open }) => ({
  ...(!open && {
    "& .MuiAccordion-root": {
      display: "grid",
      "& .MuiListItemButton-root": {
        paddingLeft: "0px",
      },
    },
  }),
  "& .MuiDrawer-paper": {
    position: "relative",
    whiteSpace: "nowrap",
    width: drawerWidth,
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
    boxSizing: "border-box",
    overflowX: "hidden",
    "& .MuiToolbar-root": {
      minHeight: HeightDefault,
    },
    ...(!open && {
      overflowX: "hidden",
      transition: theme.transitions.create("width", {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
      }),
      width: HeightDefault,
      [theme.breakpoints.up("sm")]: {
        width: HeightDefault,
      },
    }),
  },
}));

interface SidebarProps {
  open: boolean;
  onLogout?: () => void;
  toggleDrawer?: () => void;
  roleAssign: EntityRoleAssign[];
}

const Sidebar = ({
  open,
  onLogout = () => {},
  toggleDrawer = () => {},
  roleAssign = [],
}: SidebarProps) => {
  const mapCodes = roleAssign.map((role) => role.code);
  const mapRoleMenu = useMemo(() => {
    const result: MenuItemProps[] = [];
    for (const menu of Menus) {
      if (menu.type === MenuTypes.ROUTER) {
        if (mapCodes.includes(menu.role)) result.push(menu);
      } else if (menu.type === MenuTypes.ACCORDION) {
        const mapChildrens = [];
        for (const children of menu.childrens) {
          if (children?.role && mapCodes.includes(children.role))
            mapChildrens.push(children);
        }
        if (mapChildrens.length) {
          menu.childrens = mapChildrens;
          result.push(menu);
        }
      }
    }

    return result;
  }, [roleAssign, Menus]);

  if (!roleAssign) return <></>;

  return (
    <Drawer open={open} variant="permanent">
      <Toolbar
        onClick={toggleDrawer}
        className="cursor-pointer"
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          borderBottom: "1px solid #E88438",
        }}
      >
        <Link href="/" className="flex items-center flex-auto">
          <Image
            src="https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg"
            alt="logo"
            width={20}
            height={20}
          />
          <span className="font-bold text-[red]">Thesis News</span>
        </Link>
      </Toolbar>

      <div
        className="flex flex-col justify-between h-full p-0 overflow-y-auto overflow-x-hidden"
        style={{ height: "calc(100vh - 46px)" }}
      >
        <List className="p-0">
          {mapRoleMenu.map((item) => (
            // eslint-disable-next-line react/no-children-prop
            <MenuItem
              key={item.path}
              {...item}
              childrens={item?.childrens || []}
            />
          ))}
        </List>

        {/* Đăng xuất */}
        <div className="flex flex-col">
          <Divider />
          <ListItem disablePadding sx={{ border: "none" }}>
            <ListItemButton
              sx={{ "&:hover": { border: "0" } }}
              className={`!m-0 p-0 min-w-full justify-center min-h-[46px]`}
              onClick={onLogout}
            >
              <ListItemIcon className={`w-[46px] justify-center`}>
                <LogoutIcon />
              </ListItemIcon>
              <ListItemText primary="Đăng xuất" />
            </ListItemButton>
          </ListItem>
        </div>
      </div>
    </Drawer>
  );
};

export default Sidebar;
