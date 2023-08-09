import { publicApi, authorizedApi } from "./index";

// 관리자 등록
const registerAdmin = async (adminDto) => {
  try {
    const response = await publicApi.post("/admin/register", adminDto);
    return response;
  } catch (error) {
    throw new Error(`관리자 등록 에러: ${error.message}`);
  }
};

// 관리자 로그인
const login = async (adminLoginDto) => {
  try {
    const response = await publicApi.post("/admin/login", adminLoginDto);
    return response;
  } catch (error) {
    throw new Error(`관리자 로그인 에러: ${error.message}`);
  }
};

// 전체 주문 정보 가져오기
const getOrders = async () => {
  try {
    const response = await authorizedApi.get("/admin/orders");
    return response;
  } catch (error) {
    throw new Error(`전체 주문 정보 가져오기 에러: ${error.message}`);
  }
};

// Access Token 갱신
const refreshToken = async (refreshTokenDTO) => {
  try {
    const response = await authorizedApi.post("/admin/refresh", refreshTokenDTO);
    return response;
  } catch (error) {
    throw new Error(`Access Token 갱신 에러: ${error.message}`);
  }
};

// 기기 이상 알림 메세지 전송
const sendMessage = async (machineDetail) => {
  try {
    const response = await authorizedApi.get("/admin/msg", {
      params: { machine_detail: machineDetail },
    });
    return response;
  } catch (error) {
    throw new Error(`기기 이상 알림 메세지 전송 에러: ${error.message}`);
  }
};

// 물류 분석 자료
const logisticAnalysis = async () => {
  try {
    const response = await authorizedApi.get("/admin/logistic");
    return response;
  } catch (error) {
    throw new Error(`물류 분석 자료 에러: ${error.message}`);
  }
};

//사진 저장
const uploadImage = async (imageFile, logNum) => {
  try {
    const formData = new FormData();
    formData.append("image", imageFile);
    formData.append("log_num", logNum);

    //이 요청만 헤더가 재정의되어서 보내짐
    const response = await authorizedApi.post("/admin/image", formData, {
      headers: {
        //파일 업로드는 Content-Type을 multipart/form-data
        "Content-Type": "multipart/form-data",
      },
    });
    return response;
  } catch (error) {
    throw new Error(`사진 저장 에러 : ${error.message}`);
  }
};

//사진 조회
const downloadImage = async (imageName) => {
  try {
    const response = await authorizedApi.get(`/admin/image/${imageName}`);
    return response;
  } catch (error) {
    throw new Error(`사진 조회 에러 : ${error.message}`);
  }
};

const adminApi = {
  registerAdmin,
  login,
  getOrders,
  refreshToken,
  sendMessage,
  logisticAnalysis,
  uploadImage,
  downloadImage,
};

export default adminApi;
