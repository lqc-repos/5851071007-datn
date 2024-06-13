'use client'
import { Fragment, useState } from "react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { ListItemText } from "@mui/material";
import Accordion from "@mui/material/Accordion";
import AccordionDetails from "@mui/material/AccordionDetails";
import AccordionSummary from "@mui/material/AccordionSummary";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import Typography from "@mui/material/Typography";
import { usePathname } from "next/navigation";

import { MenuItemProps } from "@/types/newpost";
import { HeightDefault, MenuTypes } from "@/constant";
import { NextLinkComposed } from "./LinkIndex";

const ExpandedTypes = {
  MARKETING: "MARKETING",
};

const MenuItem = ({ Icon, path, label, type, childrens }: MenuItemProps) => {
  const router = usePathname();
  const [expanded, setExpanded] = useState("");

  const active = router?.includes(path);
  if (![MenuTypes.ACCORDION, MenuTypes.ROUTER].includes(type)) return null;

  const handleChangeExpanded = (value: string) => {
    setExpanded(value === expanded ? "" : value);
  };

  return (
    <>
      {type === MenuTypes.ROUTER ? (
        <ListItem
          key={label}
          disablePadding
          sx={{
            border: "none",
            backgroundColor: active ? "" : "bg-white",
            color: "#58585E",
          }}
        >
          <ListItemButton
            key={label}
            sx={{
              borderBottom: "1px solid #0000001F",
              "&:hover": {
                borderBottom: "1px solid #0000001F",
              },
            }}
            component={NextLinkComposed}
            to={{ pathname: `/admin${path}` }}
            className={`!m-0 !p-0 min-h-[${HeightDefault}] min-w-full justify-center`}
          >
            <ListItemIcon className="w-[46px] justify-center">
              <Icon width={18} height={18} />
            </ListItemIcon>
            <ListItemText primary={label} />
          </ListItemButton>
        </ListItem>
      ) : (
        <Accordion
          key={Math.random()}
          expanded={expanded === ExpandedTypes.MARKETING}
          onChange={() => handleChangeExpanded(ExpandedTypes.MARKETING)}
          sx={{
            boxShadow: "0",
            "& .MuiAccordionSummary-root .Mui-expanded": {
              margin: "0 !important",
            },
          }}
        >
          <AccordionSummary
            sx={{
              minHeight: "46px !important",
              borderBottom: "1px solid #0000001F",
            }}
            expandIcon={<ExpandMoreIcon />}
          >
            <Icon width={18} height={18} />
            <Typography className="pl-4 text-[#58585E]">{label}</Typography>
          </AccordionSummary>

          <AccordionDetails
            sx={{
              padding: "0px",
              border: "none",
              color: "#58585E",
            }}
          >
            {childrens &&
              childrens.map((children, index) => {
                // eslint-disable-next-line react/no-array-index-key
                if (!children.show) return <Fragment key={index} />;
                // eslint-disable-next-line react-hooks/exhaustive-deps, no-shadow
                const { Icon, path, label } = children;
                // eslint-disable-next-line react-hooks/exhaustive-deps, no-shadow
                const active = router?.includes(path);

                return (
                  // eslint-disable-next-line react/no-array-index-key
                  <ListItem
                    // eslint-disable-next-line react/no-array-index-key
                    key={index}
                    disablePadding
                    sx={{
                      backgroundColor: active ? "" : "bg-white",
                      fontSize: "12px",
                    }}
                  >
                    <ListItemButton
                      sx={{
                        borderBottom: "1px solid #0000001F",
                        "&:hover": { borderBottom: "1px solid #0000001F" },
                        "& .MuiTypography-root": { fontSize: "13px" },
                      }}
                      component={NextLinkComposed}
                      to={{ pathname: path }}
                      className={`m-0 p-0 pl-[12px] min-h-[${HeightDefault}] min-w-full justify-center`}
                    >
                      <ListItemIcon className="w-[46px] justify-center">
                        {Icon && (
                          <Icon width={18} height={18} />
                        )}
                      </ListItemIcon>
                      <ListItemText primary={label} sx={{ fontSize: "12px" }} />
                    </ListItemButton>
                  </ListItem>
                );
              })}
          </AccordionDetails>
        </Accordion>
      )}
    </>
  );
};

export default MenuItem;
