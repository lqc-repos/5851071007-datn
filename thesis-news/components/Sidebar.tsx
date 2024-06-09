// 'use client'
// import { MenuTypes, Menus } from "@/constant";
// import { MenuItemProps } from "@/types/newpost";
// import { Divider, Drawer, List, MenuItem, Toolbar } from "@mui/material";
// import { useMemo } from "react";
// import HomeIcon from '@mui/icons-material/Home';

// const Sidebar = ({
//     open,
//     onLogout = () => { },
//     toggleDrawer = () => { },
//     roleAssign = [],
// }: any) => {
//     const mapCodes = roleAssign.map((role: any) => role.code);
//     const mapRoleMenu = useMemo(() => {
//         const result: MenuItemProps[] = [];
//         for (const menu of Menus) {
//             if (menu.type === MenuTypes.ROUTER) {
//                 if (mapCodes.includes(menu.role)) result.push(menu);
//             } else if (menu.type === MenuTypes.ACCORDION) {
//                 const mapChildrens = [];
//                 for (const children of menu.childrens) {
//                     if (children?.role && mapCodes.includes(children.role)) mapChildrens.push(children);
//                 }
//                 if (mapChildrens.length) {
//                     menu.childrens = mapChildrens;
//                     result.push(menu);
//                 }
//             }
//         }

//         return result;
//     }, [roleAssign, Menus]);

//     if (!roleAssign) return <></>;

//     return (
//         <Drawer open={open} variant="permanent">
//             <Toolbar
//                 onClick={toggleDrawer}
//                 className="cursor-pointer"
//                 sx={{
//                     display: 'flex',
//                     alignItems: 'center',
//                     justifyContent: 'center',
//                     borderBottom: '1px solid #E88438',
//                 }}
//             >
//                 <HomeIcon />
//             </Toolbar>

//             <div
//                 className="flex flex-col justify-between h-full p-0 overflow-y-auto overflow-x-hidden"
//                 style={{ height: 'calc(100vh - 46px)' }}
//             >
//                 <List className="p-0">
//                     { mapRoleMenu.map((item) => (
//                         // eslint-disable-next-line react/no-children-prop
//                         <MenuItem key={item.path} {...item} childrens={item?.childrens || []} />
//                     ))}
//                 </List>

//                 {/* Đăng xuất */}
//                 <div className="flex flex-col">
//                     <Divider />
//                     <ListItem
//                         disablePadding
//                         sx={{ border: 'none' }}
//                     >
//                         <ListItemButton
//                             sx={{ '&:hover': { border: '0' } }}
//                             className={`!m-0 p-0 min-w-full justify-center min-h-[46px] text-[${PRIMARY_COLOR}]`}
//                             onClick={onLogout}
//                         >
//                             <ListItemIcon className={`w-[46px] justify-center text-[${PRIMARY_COLOR}]`}>
//                                 <LogoutIcon />
//                             </ListItemIcon>
//                             <ListItemText primary="Đăng xuất" />
//                         </ListItemButton>
//                     </ListItem>
//                 </div>
//             </div>
//         </Drawer>
//     );
// };

// export default Sidebar;