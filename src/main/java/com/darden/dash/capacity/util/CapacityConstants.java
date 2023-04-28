package com.darden.dash.capacity.util;

import java.math.BigInteger;

/**
 * @author skashala
 * @date 16-May-2022
 *
 *       This file define all the Constants.
 */

public class CapacityConstants {

	public static final String EC_4421 = "4421";
	public static final String EC_4500 = "4500";
	public static final String COM_DARDEN_DASH_CAPACITY_CONTROLLER = "com.darden.dash.capacity.controller";
	public static final String SCAN_PACKAGE = "com.darden.dash";
	public static final String ENTITY_PACKAGE = "com.darden.dash.common.entity";
	public static final String CAPACITY_MANAGEMENT_PACKAGE = "com.darden.dash.capacity";
	public static final String CAPACITY_CHANNEL3 = "capacityChannel";
	public static final String CAPACITY_CHANNEL2 = "capacityChannel2";
	public static final String CAPACITY_CHANNEL1 = "capacityChannel1";
	public static final String OPERATIONAL_HOURS_START_TIME = "operational_hours_start_time";
	public static final String OPERATIONAL_HOURS_END_TIME = "operational_hours_end_time";
	public static final String LAST_MODIFIED_DATETIME = "last_modified_datetime";
	public static final String LAST_MODIFIED_BY = "last_modified_by";
	public static final String IS_DELETED_FLG = "is_deleted_flg";
	public static final String IS_COMBINED_FLG = "is_combined_flg";
	public static final String POS_NM = "pos_nm";
	public static final String CREATED_DATETIME = "created_datetime";
	public static final String CREATED_BY = "created_by";
	public static final String CONCEPT_ID = "concept_id";
	public static final String CAPACITY_CHANNEL_NM = "capacity_channel_nm";
	public static final String CAPACITY_CHANNEL_ID = "capacity_channel_id";
	public static final String CAPACITY_CHANNEL = "capacity_channel";
	public static final String COMBINED_CAPACITY_CHANNEL_ID = "combined_capacity_channel_id";
	public static final String CAPACITY_CHANNEL_AND_COMBINED_CHANNEL = "capacity_channel_and_combined_channel";
	public static final String CAPACITY_CHANNEL_AND_ORDER_CHANNEL = "capacity_channel_and_order_channel";
	public static final String ORDER_CHANNEL_ID = "order_channel_id";
	public static final String CAPACITY_MODEL2 = "capacityModel";
	public static final String CAPACITY_MODEL_NM = "capacity_model_nm";
	public static final String CAPACITY_MODEL_ID = "capacity_model_id";
	public static final String CAPACITY_MODEL = "capacity_model";
	public static final String CAPACITY_TEMPLATE_ID = "capacity_template_id";
	public static final String CAPACITY_MODEL_AND_CAPACITY_TEMPLATE = "capacity_model_and_capacity_template";
	public static final String CAPACITY_MODEL_AND_LOCATION = "overriding_capacity_model_and_location";
	public static final String LOCATION_ID = "location_id";
	public static final String CAPACITY_SLOT_STATUS_REF_ID = "capacity_slot_status_ref_id";
	public static final String CAPACITY_SLOT_TYPE_ID = "capacity_slot_type_id";
	public static final String START_TIME = "start_time";
	public static final String END_TIME = "end_time";
	public static final String CAPACITY_CNT = "capacity_cnt";
	public static final String CAPACITY_SLOT_ID = "capacity_slot_id";
	public static final String CAPACITY_SLOT = "capacity_slot";
	public static final String PARAM_VALUE = "param_value";
	public static final String PARAM_KEY = "param_key";
	public static final String CAPACITY_SLOT_CALC_PARAM_ID = "capacity_slot_calc_param_id";
	public static final String CAPACITY_SLOT_CALC_PARAM = "capacity_slot_calc_param";
	public static final String CAPACITY_SLOT_TYPE2 = "capacitySlotType";
	public static final String CAPACITY_SLOT_TYPE_NM = "capacity_slot_type_nm";
	public static final String CAPACITY_SLOT_TYPE = "capacity_slot_type";
	public static final String CAPACITY_TEMPLATE_TYPE_ID = "capacity_template_type_id";
	public static final String CAPACITY_TEMPLATE2 = "capacityTemplate";
	public static final String WED_FLG = "wed_flg";
	public static final String TUE_FLG = "tue_flg";
	public static final String THU_FLG = "thu_flg";
	public static final String SUN_FLG = "sun_flg";
	public static final String SAT_FLG = "sat_flg";
	public static final String MON_FLG = "mon_flg";
	public static final String FRI_FLG = "fri_flg";
	public static final String EFFECTIVE_START_DATE = "effective_start_date";
	public static final String EFFECTIVE_END_DATE = "effective_end_date";
	public static final String CAPACITY_TEMPLATE_NM = "capacity_template_nm";
	public static final String CAPACITY_TEMPLATE = "capacity_template";
	public static final String CAPACITY_TEMPLATE_AND_BUSINESS_DATE = "capacity_template_and_business_date";
	public static final String BUSINESS_DATE = "business_date";
	public static final String CAPACITY_TEMPLATE_AND_CAPACITY_CHANNEL = "capacity_template_and_capacity_channel";
	public static final String CAPACITY_TEMPLATE_TYPE3 = "capacityTemplateType";
	public static final String CAPACITY_TEMPLATE_TYPE_NM = "capacity_template_type_nm";
	public static final String CAPACITY_TEMPLATE_TYPE = "capacity_template_type";
	public static final String EFFECTIVE_START_DATETIME = "effective_start_datetime";
	public static final String EFFECTIVE_LAST_DATETIME = "effective_last_datetime";
	public static final String REFERENCE_CD = "reference_cd";
	public static final String REFERENCE_DESC = "reference_desc";
	public static final String REFERENCE_NM = "reference_nm";
	public static final String REFERENCE_TYPE_ID = "reference_type_id";
	public static final String REFERENCE_ID = "reference_id";
	public static final String REFERENCE = "reference";
	public static final String REFERENCE_TYPE2 = "referenceType";
	public static final String REFERENCE_TYPE_NM = "reference_type_nm";
	public static final String REFERENCE_TYPE = "reference_type";
	public static final String INTERVAL = "interval";
	public static final String EFFECTIVE_DATE = "effective_date";
	public static final String EXPIRY_DATE = "expiry_date";
	public static final String CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY = "capacity template loaded successfully";
	public static final String CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY = "capacity template created successfully";
	public static final Integer STATUS_CODE_200 = 200;
	public static final Integer STATUS_CODE_INT_201 = 201;
	public static final Integer STATUS_CODE_INT_202 = 202;

	public static final String BEARER_ACCESS_TOKEN = "Bearer {accessToken}";

	public static final String AUTHORIZATION = "Authorization";

	public static final String METHOD_NOT_ALLOWED = "Method Not Allowed";

	public static final String BAD_REQUEST = "Bad Request";

	public static final String STATUS_CODE_405 = "405";

	public static final String STATUS_CODE_401 = "401";

	public static final String STATUS_CODE_400 = "400";

	public static final String STATUS_CODE_201 = "201";

	public static final String STATUS_CODE_202 = "202";

	public static final String STATUS_CODE_SUCCESS = "200";

	public static final String API_V1 = "api/v1";

	public static final String CAPACITY_TEMPLATES = "/capacity-templates";

	public static final String COMBINED_CHANNELS = "/combine-channels";

	public static final String SLASH = "/";

	public static final String CAPACITYMANAGEMENT_CROSS_ORIGIN = "${cross.origin}";

	public static final String MAPPER_COMBINED_CHANNELS = "combinedChannels";

	public static final String MAPPER_CAPACITY_CHANNEL_NAME = "capacityChannelName";

	public static final String MAPPER_CAPACITY_CHANNEL_NM = "capacityChannelNm";

	public static final String MAPPER_OPERATIONAL_HOURS_END_TIME = "operationalHoursEndTime";

	public static final String MAPPER_OPERATIONAL_HOURS_START_TIME = "operationalHoursStartTime";

	public static final String Y = "Y";

	public static final String N = "N";

	public static final String DATES = "Dates";

	public static final String DAYS = "Days";

	public static final String CORRELATION_ID = "Correlation Id";

	public static final String D64CF01B_CE65_4A57_AC3E_F7FA09E1A87F = "d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f";

	public static final String SUCCESS = "Success";

	public static final String MATCHES_THE_HTTP_STATUS_CODE = "Matches the http status code";

	public static final String SAT_DAY = "satDay";

	public static final String MAP_SAT_FLG = "satFlg";

	public static final String FRI_DAY = "friDay";

	public static final String MAP_FRI_FLG = "friFlg";

	public static final String THU_DAY = "thuDay";

	public static final String MAP_THU_FLG = "thuFlg";

	public static final String WED_DAY = "wedDay";

	public static final String MAP_WED_FLG = "wedFlg";

	public static final String TUE_DAY = "tueDay";

	public static final String MAP_TUE_FLG = "tueFlg";

	public static final String MON_DAY = "monDay";

	public static final String MAP_MON_FLG = "monFlg";

	public static final String SUN_DAY = "sunDay";

	public static final String MAP_SUN_FLG = "sunFlg";

	public static final String MAP_EXPIRY_DATE = "expiryDate";

	public static final String TEMPLATE_NAME = "templateName";

	public static final String MAP_CAPACITY_TEMPLATE_NM = "capacityTemplateNm";

	public static final String MAP_EFFECTIVE_DATE = "effectiveDate";

	public static final String SLOT_START_TIME = "slotStartTime";

	public static final String SLOT_END_TIME = "slotEndTime";

	public static final String SLOT_CHANNELS = "slotChannels";

	public static final String CHANNELS = "channels";

	public static final String UTC = "UTC";

	public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	public static final String MM_DD_YYYY = "MM/dd/yyyy";

	public static final String H_MM = "h:mm";

	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	public static final BigInteger BIG_INT_CONSTANT = BigInteger.valueOf(6);

	public static final String MAP_TO_TEMPLATE_ENTITY = "mapToCapacityTemplateEntity";

	public static final String MAP_TO_BUSSINESS_DATE = "mapToCapacityTemplateAndBusinessDateEntity";

	public static final String MAP_TO_SLOT_ENTITY = "mapToCapacitySlotEntity";

	public static final String MAP_TO_TEMPLATE_CHANNEL_ENTITY = "mapToCapacityTemplateAndCapacityChannelEntity";

	public static final String MAP_TO_TEMPLATE_RESPONSE = "mapToCreateTemplateResponse";

	public static final String MAP_TO_SLOT = "mapToSlotDetail";

	public static final String PATTERN_CHAR_Y_AND_CHAR_N = "^[YN]$";

	public static final String CHANNEL_UPDATED = "Channel was updated";

	public static final String CAPACITYCHANNELID = "capacityChannelId";

	public static final String POSNAME = "posName";

	public static final String INTERVAL_MIN = "Interval should be Minimum value is 5";

	public static final String INTERVAL_MAX = "Interval should be Maximum value is 1440";

	public static final String INT_EXAMPLE = "123";

	public static final String TIME_EXAMPLE = "HH:mm:ss";

	public static final String CAPACITY_SOFT_DELETE = "CAPACITY_SOFT_DELETE";

	public static final boolean FALSE = false;

	public static final String CAPACITY_TEMPLATE_WITH_TEMPLATEID = "/capacity-templates/{templateId}";

	public static final String CAPACITY_TEMPLATE_DELETED = " deleted successfully";

	public static final String APP_PARAMETER_SERVICE = "appParameterService";

	public static final String EC_4501 = "4501";

	public static final String COMBINE_CHANNELS = "/combine-channels";

	public static final String MAP_TO_CHANNEL_ENTITY = "mapToChannelEntity";

	public static final String MAP_TO_CHANNEL_AND_COMBINED_CHANNEL_ENTITY = "mapToCapacityChannelAndCombinedChannelEntity";

	public static final String MAP_TO_COMBINE_CHANNEL_RESPONSE = "maptoCombineChannelResponse";

	public static final String CHANNELS_MUST_BE_MORE_THAN_ONE = "channel more than 1";

	public static final String CAPACITY_CHANNELS_CREATED_SUCCESSFULLY = "Combine channel created successfully";

	public static final String CAPACITY_TEMPLATE_UPDATED_SUCCESSFULLY = "capacity template updated successfully";

	public static final String CAPACITY_TEMPLATE_SINGLE_API_TITLE = "One capacity template loading successfully";

	public static final String EDIT_TEMPLATES = "/capacity-templates/{templateId}";

	public static final String GET_TEMPLATES = "/capacity-templates/{templateId}";

	public static final String STATUS_CODE_UPDATED = "202";

	public static final String INTEGER = "Integer";

	public static final String EC_4502 = "4502";

	public static final String PATTERN_ALPHANUMERIC_EXCLUDING= "^[a-zA-Z0-9\\_\\. \\- \\s]+$";

	public static final String PATTERN_ALPHANUMERIC_WITH_ALL_SPL_CHARACTERS = "^[a-zA-Z0-9\\@\\# \\$ \\! \\& \\- \\_ \\. \\s]+$";

	public static final String CAPACITY_MODELS = "/capacity-models";

	public static final String CAPACITY_MODEL_LOADED_SUCCESSFULLY = "Capacity template models loaded successfully";

	public static final String EC_4504 = "4504";

	public static final String MAP_TO_CAPACITY_MODEL = "mapToCapacityModel";

	public static final String FEILD_CAPACITY_TEMPLATE_ID = "capacityTemplateId";

	public static final String FEILD_CAPACITY_TEMPLATE_NAME = "capacityTemplateName";

	public static final String EC_4503 = "4503";

	public static final String EC_4505 = "4505";

	public static final String EC_4506 = "4506";

	public static final String CAPACITY_MODELS_WITH_MODEL_ID = "/capacity-models/{modelId}";

	public static final String CAPACITY_MODEL_UPDATED_SUCCESSFULLY = "capacity template model updated successfully";

	public static final String HEADER_CONCEPT_ID = "Concept-Id";

	public static final String CAPACITY_MODEL_LIST = "/capacity-model-list/{templateId}";
	public static final String CAPACITY_MODEL_READY_FOR_DELETE = " ready for delete";
	public static final String ORDER_TEMPLATE_TYPE = "Capacity Model List";
	public static final String RESTAURANT_LIST = "Restaurants";
	public static final String ORDER_TEMPLATE = "Order Template";

	public static final String CAPACITY_CHANNEL_WITH_CHANNELID = "/capacity-channel/{channelId}";
	public static final String CAPACITY_CHANNEL_DELETED = " deleted successfully";
	public static final String RESTAURANTS_CACHE = "restaurants";
	public static final String CAPACITY_CHANNEL_CACHENAME = "capacityChannel";
	public static final String CAPACITY_CHANNEL_CACHE_KEY = "#editChannelInformationRequest.toString()";
	public static final String CAPACITY_CHANNELS_CACHE_KEY = "#channelId";
	public static final String CAPACITY_TEMPLATE_CACHE = "capacityTemplate";
	public static final String CAPACITY_TEMPLATE_CACHE_KEY = "#templateId";
	public static final String CAPACITY_MODEL_CACHE = "capacityModels";
	public static final String CAPACITY_MODEL_CACHE_KEY = "#modelId";
	public static final String CONCEPTS_CACHE = "concepts";

	public static final String MAPTOCAPACITYCHANNELENTITY = "mapToCapacityChannelEntity";
	public static final String MAPTOCAPACITYCHANNELENTITYLIST = "mapToCapacityChannelEntityList";
	public static final String MAPTOCAPACITYMODELANDLOCATIONENTITYLIST = "mapToCapacityModelAndLocationEntityList";
	public static final String MAPCREATETEMPLATERESPONSE = "mapCreateTemplateResponse";
	public static final String CAPACITYTEMPLATETYPE = "capacityTemplateType";
	public static final String BUSINESSDATE = "businessDate";
	public static final String NULL = null;
	public static final String MAPTOCAPACITYMODELENTITY = "mapToCapacityModelEntity";
	public static final String MAPTORESTAURANTASSIGNEDLIST = "mapToRestaurantAssignedList";
	public static final String MAPTOCAPACITYTEMPLATEMODEL = "mapToCapacityTemplateModel";
	public static final String MAPTOCAPACITYMODELANDLOCATIONENTITY = "mapToCapacityModelAndLocationEntity";
	public static final String MAPTOCAPACITYMODELANDCAPACITYTEMPLATEENTITYLIST = "mapToCapacityModelAndCapacityTemplateEntityList";
	public static final String MAPTOCAPACITYMODELANDCAPACITYTEMPLATEENTITY = "mapToCapacityModelAndCapacityTemplateEntity";
	public static final String MAPTOUPDATECAPACITYTEMPLATESLOTS = "mapToUpdateCapacityTemplateSlots";
	public static final String MAPTOUPDATESLOTCHANNELRESPONSE = "mapToUpdateSlotChannelResponse";
	public static final String MAPTEMPLATEDAYSFROMTEMPLATECREATEUPDATEREQUEST = "mapTemplateDaysFromTemplateCreateUpdateRequest";
	public static final String SETTEMPLATEDAYSTONULLVALUE = "setTemplateDaysToNullValue";
	public static final String MAPTOCAPACITYSLOTENTITY = "mapToCapacitySlotEntity";
	public static final String MAPTOCAPACITYTEMPLATEFROMENTITY = "mapToCapacityTemplateFromEntity";
	public static final String GETCAPACITYTEMPLATECHANNELS = "getCapacityTemplateChannels";
	public static final String MAPCAPACITYSLOTS = "mapCapacitySlots";
	public static final String MAPSLOTCHANNELS = "mapSlotChannels";
	public static final String MAPTEMPLATETYPERESPONSE = "mapTemplateTypeResponse";
	public static final String BLANK = "";
	public static final String CAPACITYMODELLIST = "/capacitymodel-list";
	public static final String ALL_MODELS_LOADED_SUCESSFULLY = "All template model relating to capacity template loaded successfully";
	public static final String EC_4507 = "4507";
	public static final String CAPACITY_TEMPLATE_MODEL_ID = "capacity template model id";
	public static final String COMBINE_CAPACITY_TEMPLATE_CACHE_KEY = "{#isRefDataReq, #conceptId}";
	public static final String MODEL = "Capacity template models";
	public static final String LOCATION_CONNECTION = "Location connection ";
	public static final String CAPACITY_CHANNELS_PATH = "/capacity-channels";
	public static final String CAPACITY_CHANNELS_LOADED_SUCCESSFULLY = "Capacity channels loaded successfully";
	public static final String FAILED_TO_ADD_DATA_TO_AUDIT = "failed to add data to audit";
	public static final String IS_CAPACITY_MODEL_DATA_REQ_FALSE = "?isCapacityModelDataReq=false";

	public static final String CHANNEL_WAS_UPDATED = "Channel was updated";
	public static final String CONCEPT_ID_CACHE_KEY = "#conceptId";
	public static final String PATTERN_ALPHANUMERIC_GLOBAL_SPL_CHARACTERS = "^[a-zA-Z0-9- @ # $ ! & _ . * ( ) + , < > / ? ' % s]*+$";

	//foh capacity template controller
	public static final String MSG_REFERENCE_DATA_LOADED_SUCESSFULLY = "reference data loaded sucessfully";
	public static final String UA_CAPACITY_MANAGER_SLOTS_VIEW = "CapacityManager.Slots.View";
	public static final String PATH_SLASH = "/";
	public static final String PATH_FOH_CAPACITY_TEMPLATES = PATH_SLASH+"foh-capacity-templates";
	public static final String PATH_FOH_REFERENCEDATA = PATH_SLASH+"foh-referencedata";

	public static final String SLOT_UPDATE = "/slots";

	public static final String SLOT_UPDATE_RESPONSE = "Capacity slot updated successfully";

	public static final String CAPACITY_SLOT_EDIT = "CapacityManager.Slots.Edit";

	public static final String SLOT_UPDATED = "Slot updated";

	private CapacityConstants() {

	}

}
