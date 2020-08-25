using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace HyeonhoApp
{
    public partial class Form2 : Form
    {
        private ListView listView_schedule;
        public Form2(ListView listView_schedule)
        {
            this.listView_schedule = listView_schedule;
            InitializeComponent();
            reset();
            label_nowTime.Text = "현재시각 " + DateTime.Now.ToString("HH:mm:ss"); // 현재시각 label 초기화
            monthCalendar1.MinDate = DateTime.Today; // 오늘 이전날짜 비활성화
        }

        private void reset()
        {
            // Form2 의 reset
            textBox_scheduleName.Text = listView_schedule.SelectedItems[0].SubItems[2].Text; // 일정제목
            textBox_scheduleContents.Text = listView_schedule.SelectedItems[0].SubItems[3].Text; // 일정내용 초기화
            comboBox_startTime.SelectedItem = listView_schedule.SelectedItems[0].SubItems[1].Text.Split('~')[0];
            comboBox_endTime.SelectedItem = listView_schedule.SelectedItems[0].SubItems[1].Text.Split('~')[1]; // 일정 종료시간 초기화
            monthCalendar1.SetDate(DateTime.Parse(listView_schedule.SelectedItems[0].SubItems[0].Text)); // calendar 오늘 날짜로 이동
        }

        private void button_addSchedule_Click(object sender, EventArgs e)
        {
            if (String.IsNullOrWhiteSpace(textBox_scheduleName.Text))
            {
                MessageBox.Show("일정을 입력하세요!");
            }

            else
            {
                ListViewItem newItem = new ListViewItem(monthCalendar1.SelectionRange.Start.ToShortDateString());
                newItem.SubItems.Add(comboBox_startTime.SelectedItem.ToString() + "~" + comboBox_endTime.SelectedItem.ToString());
                newItem.SubItems.Add(textBox_scheduleName.Text);
                newItem.SubItems.Add(textBox_scheduleContents.Text);
                listView_schedule.Items[listView_schedule.SelectedIndices[0]] = newItem; // 일정 수정
                MessageBox.Show("일정 수정 완료");
                Application.OpenForms["Form2"].Close();
            }
        }

        private void comboBox_startTime_SelectedIndexChanged(object sender, EventArgs e)
        {
            comboBox_endTime.Items.Clear();
            for (int i = comboBox_startTime.SelectedIndex; i < comboBox_startTime.Items.Count; i++)
            {
                comboBox_endTime.Items.Add(comboBox_startTime.Items[i]);
            }
            comboBox_endTime.SelectedIndex = 1;
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            label_nowTime.Text = "현재시각 " + DateTime.Now.ToString("HH:mm:ss"); // 현재시각을 갱신해주는 timer. 1초마다 refresh
        }
    }
}
