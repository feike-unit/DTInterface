package com.izerui.utils;
public enum DTenmun{
		/**
		 * 发文(0)
		 */
		DT_FAWEN(0),
		/**
		 * 收文(1)
		 */
		DT_SHOUWEN(1),
		/**
		 * 请示报告(2)
		 */
		DT_QINGSHI(2),
		/**
		 * 下来文(3)
		 */
		DT_XIALAIWEN(3);
		
		DTenmun(Integer value) {
			this.value = value;
		}

		private Integer value;

		public Integer getValue() {
			return value;
		}
		
		public String getFtpRootPath(){
			switch (value) {
			case 0:
				return "NEW_FW";
			case 1:
				return "NEW_SW";
			case 2:
				return "NEW_BG";
			case 3:
				return "NEW_X_SW";
			default:
				return "NEW_OTHER";
			}
		}
		
		public String getTable(){
			switch (value) {
			case 0:
				return "W_QT14";
			case 1:
				return "W_QT13";
			case 2:
				return "W_QT15";
			case 3:
				return "W_QT13";
			}
			return null;
		}
		public String getETable(){
			switch (value) {
			case 0:
				return "E_FILEQT14";
			case 1:
				return "E_FILEQT13";
			case 2:
				return "E_FILEQT15";
			case 3://下来文
				return "E_FILEQT13";
			}
			return null;
		}
		
		public String getOATable(){
			switch (value) {
			case 0:
				return "T_HZOA_APP_DTFW";
			case 1:
				return "T_HZOA_APP_DTSW";
			case 2:
				return "T_HZOA_APP_QSBG";
			case 3://下来文
				return "T_HZOA_APP_XJQS";
			}
			return null;
		}

		public String getOAFieldName() {
			switch (value) {
			case 0:
				return "F2";
			case 1:
				return "F7";
			case 2:
				return "F3";
			case 3:
				return "F7";//下来文也对应到收文中
			}
			return null;
		}
		
	}

