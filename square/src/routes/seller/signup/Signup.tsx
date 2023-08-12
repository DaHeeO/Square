/* eslint-disable no-alert */
/* eslint-disable no-console */
import React, { useState } from "react";
import axios from "axios";
import DaumPostcodeEmbed from "react-daum-postcode";
import { Address } from "react-daum-postcode/lib/loadPostcode";

declare global {
  interface Window {
    kakao: any; // 또는 kakao의 실제 타입을 지정
  }
}

export default function SignUp() {
  const [name, setName] = useState<string>(""); // 타입 명시
  const [id, setId] = useState<string>(""); // 타입 명시
  const [pw, setPw] = useState<string>(""); // 타입 명시
  const [nameValid, setNameValid] = useState(false);

  const [id, setId] = useState<string>("");
  const [idValid, setIdValid] = useState(false);
  const [isIdDuplicated, setIsIdDuplicated] = useState(false);
  const [pwValid, setPwValid] = useState(false);
  const [notAllow, setNotAllow] = useState(true);

  const [email, setEmail] = useState<string>("");
  const [emailValid, setEmailValid] = useState(false);

  const [nickname, setNickname] = useState<string>("");
  const [nicknameValid, setNicknameValid] = useState(false);

  const [phoneNumber, setPhoneNumber] = useState<string>("");
  const [phoneNumberValid, setPhoneNumberValid] = useState(false);

  const [marketing, setMarketing] = useState<boolean>(false);
  
  const [bcode, setBcode] = useState<string>("")
  const [address, setAddress] = useState<string>("")
  const [storeName, setStoreName] = useState<string>("")
  const [storePhone, setStorePhone] = useState<string>("")
  const [storePhoneValid, setStorePhoneValid] = useState(false);

  const [content, setContent] = useState<string>("")
  const [bank, setBank] = useState<string>("")
  const [accountValid, setAccountValid] = useState(false)
  const [account, setAccount] = useState<string>("")

  const [latitude, setLatitude] = useState<string>("")
  const [longtitude, setLongtitude] = useState<string>("")
  
  const [saveAddress, setSaveAddress] = useState<string>("")

  // const [businessInformation, setBusinessInformation] = useState<string>("") // 하위 key-value 쌍을 어떻게 정의?

  const [companyRegistrationNumber, setCompanyRegistrationNumber] = useState<string>("")
  const [companyRegistrationNumberValid, setCompanyRegistrationNumberValid] = useState(false)
  const [ceoName, setCeoName] = useState<string>("")
  const [openingDate, setOpeningDate] = useState<string>("")
  const [openingDateValid, setOpeningDateValid] = useState(false)

  const [corporateRegistrationNumber, setCorporateRegistrationNumber] = useState<string>("")
  const [corporateRegistrationNumberValid, setCorporateRegistrationNumberValid] = useState(false)

  const [businessName, setBusinessName] = useState<string>("")
  const [businessFile, setBusinessFile] = useState<string>("")

  // 회원가입 버튼 활성화 여부 업데이트 함수
  const updateNotAllow = () => {
    setNotAllow(
      !idValid ||
      !pwValid ||
      !isValidBusinessNumber(businessNumber) ||
      !isValidRepresentativeName(representativeName) ||
      !isValidOpeningDate(openingDate)
    );
  };

  const handleName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
    setNameValid(e.target.value.length >= 2);
  };

  const handleId = (e: React.ChangeEvent<HTMLInputElement>) => {
    setId(e.target.value);
    const regex = /^[a-z]+[a-z0-9]{5,19}$/g;

    setIdValid(regex.test(e.target.value));
    updateNotAllow();
  };

  const handleCheckId = () => {
    axios
      .get(`http://i9b208.p.ssafy.io:8811/user/id/${id}`) 
      .then((response) => {
        setIsIdDuplicated(response.data.duplicated);
        updateNotAllow();
      })
      .catch((error) => {
        console.error("아이디 중복 확인 오류:", error);
      });
  };

  const handlePw = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPw(e.target.value);
    const regex =
      /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+])(?!.*[^a-zA-z0-9$`~!@$!%*#^?&\\(\\)\-_=+]).{8,20}$/;
    setPwValid(regex.test(e.target.value));
    updateNotAllow();
  };

  const handleEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
    // 이메일 유효성 검사
    const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    setEmailValid(regex.test(e.target.value));
    updateNotAllow();
  };

  const handleNickname = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
    // 닉네임 유효성 검사 (여기서는 최소 3글자 이상으로 가정)
    setNicknameValid(e.target.value.length >= 3);
    updateNotAllow();
  };

  const handlePhoneNumber = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPhoneNumber(e.target.value);
    // 전화번호 유효성 검사 (여기서는 숫자 11자리로 가정)
    const regex = /^\d{11}$/;
    setPhoneNumberValid(regex.test(e.target.value));
    updateNotAllow();
  };

  const handleAddress = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAddress(e.target.value)
    updateNotAllow();
  };

  const handleStoreName  = (e: React.ChangeEvent<HTMLInputElement>) => {
    setStoreName(e.target.value)
  };

  const handleStorePhone  = (e: React.ChangeEvent<HTMLInputElement>) => {
    setStorePhone(e.target.value);
    // 전화번호 유효성 검사 (여기서는 숫자 10자리로 가정)
    const regex = /^\d{10}$/;
    setStorePhoneValid(regex.test(e.target.value));
    updateNotAllow();
  };

  const handleContent  = (e: React.ChangeEvent<HTMLInputElement>) => {
    setContent(e.target.value)
  };

  const handleBank  = (e: React.ChangeEvent<HTMLInputElement>) => {
    setBank(e.target.value)
  };

  const handleAccount = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAccount(e.target.value)
    const regex = /^\d{10,14}$/;
    setAccountValid(regex.test(e.target.value));
    updateNotAllow();
  };

  const handleCompanyRegistrationNumber = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCompanyRegistrationNumber(e.target.value);
    // 사업자등록번호 유효성 검사 (여기서는 숫자 11자리로 가정)
    const regex = /^\d{11}$/;
    setCompanyRegistrationNumberValid(regex.test(e.target.value));
    updateNotAllow();
  };

  const handleCeoName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCeoName(e.target.value);
    updateNotAllow();
  };

  const handleOpeningDate = (e: React.ChangeEvent<HTMLInputElement>) => {
    setOpeningDate(e.target.value);
    // 오픈 날짜 유효성 검사 (여기서는 숫자 8자리로 가정)
    const regex = /^\d{8}$/;
    setOpeningDateValid(regex.test(e.target.value));
    updateNotAllow();
  };

  const handleCorporateRegistrationNumber = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCorporateRegistrationNumber(e.target.value)
    // 법인등록번호 유효성검사
    const regex = /^\d{11}$/;
    setCorporateRegistrationNumberValid(regex.test(e.target.value));
    updateNotAllow();
  };

  const handleBusinessName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setBusinessName(e.target.value);
    updateNotAllow();
  };
  const handleBusinessFile = (e: React.ChangeEvent<HTMLInputElement>) => {
    setBusinessFile(e.target.value);
    updateNotAllow();
  };

  const handleMarketing = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMarketing(e.target.checked);
    updateNotAllow();
  };
  
  const updateNotAllow = () => {
    setNotAllow(!idValid || !pwValid || !nameValid || !emailValid || !nicknameValid || !phoneNumberValid || !accountValid);
  };

  const { kakao } = window;

  const onDataHandler = (address: Address) => {
    console.log(address.address);
    setBcode(address.bcode);
    setSaveAddress(address.address);
  
    axios.get(`https://dapi.kakao.com/v2/local/search/address.json?query=${address.address}`, {
      headers: {
        Authorization: "KakaoAK 409915cf48a47370a92cea926084d5a1" // git에 올리지 말 것! api키는 항상 다른데 보관 gitignore가 되는 곳에
      }
    })
      .then(response => (
        // console.log(response.data.documents[0].y)
        setLongtitude(response.data.documents[0].x)
        // setLatitude(response.data.documents[0].y)
      ))
      .catch(error => (
        console.log(error)
      ))
  };

  const handleSignup = () => {
    const body = {
      uid: id,
      password: pw,
      name: name,
      email: email,
      nickname: nickname,
      phoneNumber: phoneNumber,
      marketing: marketing,
      bcode: bcode,
      address: address,
      storeName: storeName,
      storePhone: storePhone,
      content: content,
      bank: bank,
      latitude: latitude,
      longtitude: longtitude,
      businessInformation: {
        companyRegistrationNumber: companyRegistrationNumber,
        ceoName: ceoName,
        openingDate: openingDate,
        corporateRegistrationNumber: corporateRegistrationNumber,
        businessName: businessName,
        businessFile: businessFile,
      }
    };

    axios
    .post("http://43.201.255.188:8811/store/business-license", body)
    .then((response) => {
          if (response.data.success) {
            console.log("성공???")
            alert("회원가입에 성공했습니다.");
            window.location.href = "/login";
          } else {
            alert("이미 등록된 아이디입니다.");
          }
        })
        .catch((error) => {
          console.error("회원가입 오류:", error);
          alert("회원가입에 실패했습니다.");
        });
  };

  return (
    <div className="page">
      <div className="titleWrap">Sign up</div>
      <div className="contentWrap">
        <div className="inputTitle">이름</div>
        <div className="inputWrap">
          <input
            className="input"
            type="text"
            placeholder="홍길동"
            value={name}
            onChange={handleName}
          />
        </div>
        <div className="errorMessageWrap">
          {!nameValid && name.length > 0 && (
            <div>이름을 최소 2글자 이상 입력해주세요.</div>
          )}
        </div>

        <div style={{ marginTop: "26px" }} className="inputTitle">
          Id
        </div>
        <div className="inputWrap">
          <input
            className="input"
            type="text"
            placeholder="testid"
            value={id}
            onChange={handleId}
          />
          <button onClick={handleCheckId}>중복 확인</button>
        </div>
        <div className="errorMessageWrap">
          {isIdDuplicated && <div>이미 사용 중인 아이디입니다.</div>}
        </div>
        <div className="errorMessageWrap">
          {!idValid && id.length > 0 && (
            <div>올바른 아이디를 입력해주세요.</div>
          )}
        </div>

        <div style={{ marginTop: '26px' }} className="inputTitle">
          사업자 등록번호
        </div>
        <div className="inputWrap">
          <input
            className="input"
            type="text"
            placeholder="사업자등록번호를 입력하세요 (숫자 10자리)"
            value={companyRegistrationNumber}
            onChange={handleCompanyRegistrationNumber}
          />
        </div>
        <div className="errorMessageWrap">
          {!isValidBusinessNumber(businessNumber) && businessNumber.length > 0 && (
            <div>올바른 사업자 등록번호 형식이 아닙니다. (예: 1234567890)</div>
          )}
        </div>

        <div style={{ marginTop: "26px" }} className="inputTitle">
          대표자 성명
        </div>
        <div className="inputWrap">
          <input
            className="input"
            type="text"
            placeholder="대표자 성명을 입력하세요."
            value={ceoName}
            onChange={handleCeoName}
          />
        </div>

        <div style={{ marginTop: '26px' }} className="inputTitle">
          오픈날짜
        </div>
        <div className="inputWrap">
          <input
            className="input"
            type="text"
            placeholder="오픈날짜를 입력하세요 (숫자 8자리)"
            value={openingDate}
            onChange={handleOpeningDate}
          />
        </div>
        <div className="errorMessageWrap">
          {!isValidRepresentativeName(representativeName) && representativeName.length > 0 && (
            <div>대표자 성명을 입력해주세요.</div>
          )}
        </div>

        <div style={{ marginTop: '26px' }} className="inputTitle">
          개업일자
        </div>
        <div className="inputWrap">
          <input
            className="input"
            type="text"
            placeholder="법인등록번호를 입력하세요 (숫자 10자리)"
            value={corporateRegistrationNumber}
            onChange={handleCorporateRegistrationNumber}
          />
        </div>
        <div className="errorMessageWrap">
          {!corporateRegistrationNumberValid && corporateRegistrationNumber.length > 0 && (
            <div>올바른 법인등록번호 형식이 아닙니다. (숫자 10자리)</div>
          )}
        </div>

        {/* 이메일, 닉네임, 전화번호, 마케팅 동의 관련 코드 생략 */}

      </div>

        <div>
          <button
            onClick={handleSignup}
            disabled={notAllow}
            className="bottomButton"
          >
            가입하기
          </button>
        </div>
      </div>
    </div>
  );
}
