import * as React from 'react';
import Grid from '@mui/material/Unstable_Grid2';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import Input from '@mui/material/Input';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import FormControl from '@mui/material/FormControl';
import InputAdornment from '@mui/material/InputAdornment';
import ImagePreview from './ImagePreview';


export default function ProductForm({open, close}) {
  const [age, setAge] = React.useState('');
  const [product, setProduct] = React.useState({
    id: 0,
    image: "",
    thumbnail: "",
    category_id: 0,
		type_id: 0,
    name: "",
    represent: true,
    popular: true,
    price: 0,
    status_code: 0,
    create_at: "", 
    modified_at: "",
    description: ""
  });
  const handleChange = (event) => {
    setAge(event.target.value);
  };

  const handleProductChange = (event) => {
    const { name, value } = event.target;
    
    setProduct((prevProduct) => ({
        ...prevProduct,
        [name]: value,
      }));
  };

  return (
    <>
      <Dialog open={open} onClose={close} maxWidth="600px">
        <Box width="600px">
        <Box sx={{display: "flex", justifyContent: "flex-start", backgroundColor: "#282c34", alignItems: "center", height: "60px"}}>
          <DialogTitle sx={{padding: "0px 0px 0px 20px", fontSize: "24px", fontWeight: 500, color: "white"}}>상품 등록</DialogTitle>
        </Box>
        <DialogContent>
          <Grid container spacing={4}>
            <Grid xs={12} sx={{display: "flex", flexDirection: "column", alignItems: "center"}}>
              <DialogContentText>
                상품 이미지
              </DialogContentText>
              <ImagePreview />
            </Grid>
            <Grid xs={12}> 
            <DialogContentText>
                상품명
              </DialogContentText>
              <TextField id="name"
                name="name"
                fullWidth
                variant="outlined"
                onChange={handleProductChange}/>
            </Grid>
            <Grid xs={6}>
              <DialogContentText>
                가격
              </DialogContentText>
              <FormControl fullWidth>
                <Select
                  labelId="demo-simple-select-label"
                  id="demo-simple-select"
                  value={age}
                  size="small"
                  label="Age"
                  onChange={handleChange}
                >
                  <MenuItem value={10}>Ten</MenuItem>
                  <MenuItem value={20}>Twenty</MenuItem>
                  <MenuItem value={30}>Thirty</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid xs={6}>
              <DialogContentText>
                가격
              </DialogContentText>
              <FormControl fullWidth variant="outlined">
                <Input
                  id="outlined-adornment-amount"name="price"
                startAdornment={<InputAdornment position="start">￦</InputAdornment>}
                />
              </FormControl>
            </Grid>

            <Grid xs={6}>
              <DialogContentText>
                카테고리
              </DialogContentText>
              <FormControl fullWidth>
                <Select
                  labelId="demo-simple-select-label"
                  id="demo-simple-select"
                  value={age}
                  label="Age"
                  onChange={handleChange}
                >
                  <MenuItem value={10}>Ten</MenuItem>
                  <MenuItem value={20}>Twenty</MenuItem>
                  <MenuItem value={30}>Thirty</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid xs={3}>
              <FormGroup>
                <FormControlLabel control={<Checkbox />} label="대표" />
              </FormGroup>
            </Grid>
            <Grid xs={3}>
              <FormGroup>
                <FormControlLabel control={<Checkbox />} label="인기" />
              </FormGroup>
            </Grid>
            <Grid xs={6}>
            <DialogContentText>
                상태
              </DialogContentText>
              <FormControl fullWidth>
                <Select
                  labelId="demo-simple-select-label"
                  id="demo-simple-select"
                  value={age}
                  label="Age"
                  onChange={handleChange}
                >
                  <MenuItem value={10}>Ten</MenuItem>
                  <MenuItem value={20}>Twenty</MenuItem>
                  <MenuItem value={30}>Thirty</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid xs={12}>
              <DialogContentText>
                세부 설명
              </DialogContentText>
              <TextField
                id="outlined-multiline-static"
                fullWidth
                multiline
                rows={4}
              />
            </Grid>
            </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={close}>닫기</Button>
          <Button onClick={()=>{
            // eslint-disable-next-line no-console
            console.log(product);
            close();
          }}>등록</Button>
        </DialogActions>
        </Box>
      </Dialog>
    </>
  );
}