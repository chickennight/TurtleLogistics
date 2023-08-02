import { publicApi } from "./index";

// 기기 상태 정보 반환
const getMachineStatus = async () => {
  try {
    const response = await publicApi.get("/machine");
    return response;
  } catch (error) {
    throw new Error(`기기 상태 정보 반환 에러: ${error.message}`);
  }
};

// 기기 로그 가져오기
const getLogs = async () => {
  try {
    const response = await publicApi.get("/machine/log");
    return response;
  } catch (error) {
    throw new Error(`기기 로그 가져오기 에러: ${error.message}`);
  }
};

// 로그 생성
const createLog = async (logAddDto) => {
  try {
    const response = await publicApi.post("/machine/log", logAddDto);
    return response;
  } catch (error) {
    throw new Error(`로그 생성 에러: ${error.message}`);
  }
};

// 기기 정보 업데이트
const updateMachine = async (machineId, machineDto) => {
  try {
    const response = await publicApi.put(`/machine/${machineId}`, machineDto);
    return response;
  } catch (error) {
    throw new Error(`기기 정보 업데이트 에러: ${error.message}`);
  }
};

// 기기 제어
const machineControl = async (payloadDto) => {
  try {
    const response = await publicApi.post("/machine/control", payloadDto);
    return response;
  } catch (error) {
    throw new Error(`기기 제어 에러: ${error.message}`);
  }
};

const machineApi = {
  getMachineStatus,
  getLogs,
  createLog,
  updateMachine,
  machineControl,
};

export default machineApi;
