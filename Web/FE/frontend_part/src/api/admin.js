import { publicApi, authorizedApi } from "./axiosInstances";

// 관리자 등록
const registerAdmin = async (adminDto) => {
  try {
    const response = await publicApi.post("/admin/register", adminDto);
    return response;
  } catch (error) {
    throw error;
  }
};

// 관리자 로그인
const login = async (adminLoginDto) => {
  try {
    const response = await publicApi.post("/admin/login", adminLoginDto);
    return response;
  } catch (error) {
    throw error;
  }
};

// 전체 주문 정보 가져오기
const getOrders = async () => {
  try {
    const response = await authorizedApi.get("/admin/orders");
    return response;
  } catch (error) {
    throw error;
  }
};

// Access Token 갱신
const refreshToken = async (refreshTokenDTO) => {
  try {
    const response = await authorizedApi.post("/admin/refresh", refreshTokenDTO);
    return response;
  } catch (error) {
    throw error;
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
    throw error;
  }
};

// 물류 분석 자료
const logisticAnalysis = async () => {
  try {
    const response = await authorizedApi.get("/admin/logistic");
    return response;
  } catch (error) {
    throw error;
  }
};

const adminApi = {
  registerAdmin,
  login,
  getOrders,
  refreshToken,
  sendMessage,
  logisticAnalysis,
};

export default adminApi;
