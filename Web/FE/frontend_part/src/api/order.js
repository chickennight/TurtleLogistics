import { publicApi, authorizedApi } from "./index";

// 일자별 주문 분석
const dataAnalysisDay = async (startDay, endDay) => {
  try {
    const response = await authorizedApi.get("/order/analysis/day", {
      params: {
        start_day: startDay,
        end_day: endDay,
      },
    });
    return response;
  } catch (error) {
    throw new Error(`일자별 주문 분석 에러: ${error.message}`);
  }
};

// 지역별 주문 분석
const dataAnalysisRegion = async (regioncode) => {
  try {
    const response = await authorizedApi.get("/order/analysis/regioncode", {
      params: {
        regioncode
      },
    });
    return response;
  } catch (error) {
    throw new Error(`지역별 주문 분석 에러: ${error.message}`);
  }
};

// 포장이 필요한 주문 반환
const packageList = async () => {
  try {
    const response = await publicApi.get("/order/start");
    return response;
  } catch (error) {
    throw new Error(`포장이 필요한 주문 반환 에러: ${error.message}`);
  }
};

// 새로운 주문 입력
const newOrder = async (newOrderDto) => {
  try {
    const response = await publicApi.post("/order", newOrderDto);
    return response;
  } catch (error) {
    throw new Error(`새로운 주문 입력 에러: ${error.message}`);
  }
};

// 주문 진행 현황 업데이트
const orderUpdate = async (orderUpdateDto) => {
  try {
    const response = await publicApi.put("/order/update", orderUpdateDto);
    return response;
  } catch (error) {
    throw new Error(`주문 진행 현황 업데이트 에러: ${error.message}`);
  }
};

// 현재 진행중인 주문 목록
const orderNow = async () => {
  try {
    const response = await authorizedApi.get("/order/now");
    return response;
  } catch (error) {
    throw new Error(`현재 진행중인 주문 목록 에러: ${error.message}`);
  }
};

const orderAPI = {
  dataAnalysisDay,
  dataAnalysisRegion,
  packageList,
  newOrder,
  orderUpdate,
  orderNow,
};

export default orderAPI;
