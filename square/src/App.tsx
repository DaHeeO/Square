import { createTheme, ThemeProvider } from "@mui/material/styles";
import { Routes, Route } from "react-router-dom";
import { Unstable_Grid2 as Grid } from "@mui/material";
import { green, grey } from "@mui/material/colors";
import "./App.css";
import Seller from "./routes/seller/Seller";
import Customer from "./routes/customer/Customer1";
import Error from "./routes/error/Error";

// import CustomerSignUp from "routes/customer/signup/CustomerSignup";
// import CustomerLogin from "routes/customer/login/CustomerLogin";

const theme = createTheme({
  typography: {
    fontFamily: "Pretendard, sans-serif", // 원하는 폰트를 지정합니다.
  },
  palette: {
    primary: {
      main: green[900],
    },
    secondary: {
      main: grey[500],
    },
  },
});

function App() {
  return (
    <Grid container className="App">
      <ThemeProvider theme={theme}>
        <Grid container justifyContent="center" xs>
          <Routes>
            <Route path="/*" element={<Customer />} />
            <Route path="/seller/*" element={<Seller />} />
            <Route path="/error/*" element={<Error />} />
          </Routes>
        </Grid>
      </ThemeProvider>
    </Grid>
  );
}

export default App;
